package com.linktime.ktorbase.gateway.jira

import com.google.gson.*
import com.linktime.ktorbase.gateway.httpClient
import com.linktime.ktorbase.model.Ticket
import com.linktime.ktorbase.model.TicketData
import io.ktor.client.HttpClient
import io.ktor.client.request.*
import io.ktor.client.response.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType

object JiraApi {
    private val baseUrl = JiraConfig.baseUrl
    private val customFieldNameConfig = JiraConfig.CustomFieldNameConfig

    private val jiraHttpClient =
        httpClient(JiraConfig.username, JiraConfig.password)

    suspend fun login(username: String, password: String): String? {
        val response = jiraHttpClient.post<HttpResponse> {
            url("$baseUrl/rest/auth/1/session")
            contentType(ContentType.Application.Json)
            body = LoginRequestBody(username, password)
        }
        return if (response.status.value == 200) {
            username
        } else {
            null
        }
    }

    /**
     * @return issue key
     */
    suspend fun createIssue(
        summary: String,
        assignee: String,
        eventId: String?
    ): String {
        val projectKey = JiraConfig.projectKey
        val issueTypeName = JiraConfig.issueTypeName
        val customFieldKeys = getCustomFieldKeys(jiraHttpClient, projectKey, issueTypeName)

        val projectJson = JsonObject()
        projectJson.addProperty("key", projectKey)

        val issueTypeJson = JsonObject()
        issueTypeJson.addProperty("name", issueTypeName)

        val assigneeJson = JsonObject()
        assigneeJson.addProperty("name", assignee)

        val fieldsJson = JsonObject()
        fieldsJson.add("project", projectJson)
        fieldsJson.addProperty("summary", summary)
        fieldsJson.add("issuetype", issueTypeJson)
        fieldsJson.add("assignee", assigneeJson)
        fieldsJson.add(
            customFieldKeys.getValue(customFieldNameConfig.incidentId),
            createStringCustomFieldJson(eventId)
        )

        val jsonBody = JsonObject()
        jsonBody.add("fields", fieldsJson)

        val jsonResponse = jiraHttpClient.post<String> {
            url("$baseUrl/rest/api/2/issue")
            contentType(ContentType.Application.Json)
            body = jsonBody
        }
        return JsonParser().parse(jsonResponse)
            .asJsonObject
            .getAsJsonPrimitive("key").asString
    }

    suspend fun getIssue(issueKey: String): Ticket? {
        val cfNameConfig = JiraConfig.CustomFieldNameConfig
        val jsonResponse = jiraHttpClient.get<String> {
            url("$baseUrl/rest/api/2/issue/$issueKey")
            parameter("expand", "names")
        }
        val jsonObject = JsonParser().parse(jsonResponse).asJsonObject
        val customFieldKeys = jsonObject
            .getAsJsonObject("names")
            .entrySet()
            .filter { it.key.startsWith("customfield") }
            .map { it.value.asString to it.key }
            .toMap()
        val fieldsObject = jsonObject.getAsJsonObject("fields")

        fun insightObjectIdFromCfValue(cfName: String): Int? {
            val cfKey = customFieldKeys.getValue(cfName)
            val cfValue = fieldsObject.get(cfKey)
            return cfValue
                .takeIf { it.isJsonArray }
                ?.asJsonArray
                ?.firstOrNull()
                ?.asString
                ?.toIntOrNull()
        }

        fun stringFromFieldValue(name: String): String? =
            fieldsObject.get(name).takeIf { it.isJsonPrimitive }?.asString

        fun stringFromCfValue(cfName: String): String? {
            val cfKey = customFieldKeys.getValue(cfName)
            return stringFromFieldValue(cfKey)
        }

        fun booleanFromYesNoCfValue(cfName: String): Boolean? {
            val cfKey = customFieldKeys.getValue(cfName)
            val cfValue = fieldsObject.get(cfKey)
            return cfValue
                .takeIf { it.isJsonObject }
                ?.asJsonObject
                ?.getAsJsonPrimitive("value")?.asString
                ?.let { it == "yes" }
        }

        fun listFromCfValue(cfName: String): List<Int>? {
            val cfKey = customFieldKeys.getValue(cfName)
            return stringFromFieldValue(cfKey)
                ?.split(",")
                ?.mapNotNull { it.trim().toIntOrNull() }
        }

        val status = fieldsObject.getAsJsonObject("status").getAsJsonPrimitive("name").asString

        return Ticket(
            issueKey,
            closed = status == JiraConfig.Workflow.StatusName.closed,
            data = TicketData(
                serviceId = insightObjectIdFromCfValue(cfNameConfig.service),
                summary = stringFromFieldValue("summary"),
                description = stringFromFieldValue("description"),
                incidentId = stringFromCfValue(cfNameConfig.incidentId)!!
            )
        )
    }

    suspend fun updateIssue(issueKey: String, data: TicketData) {
        val projectKey = JiraConfig.projectKey
        val issueTypeName = JiraConfig.issueTypeName
        val customFieldKeys = getCustomFieldKeys(jiraHttpClient, projectKey, issueTypeName)
        val fieldsJson = JsonObject()

        fieldsJson.add(
            customFieldKeys.getValue(customFieldNameConfig.service),
            createInsightObjectsJsonArray(data.serviceId)
        )
        fieldsJson.add(
            "summary",
            createStringCustomFieldJson(data.summary)
        )
        fieldsJson.add(
            "description",
            createStringCustomFieldJson(data.description)
        )

        val jsonBody = JsonObject()
        jsonBody.add("fields", fieldsJson)

        return jiraHttpClient.put {
            url("$baseUrl/rest/api/2/issue/$issueKey")
            contentType(ContentType.Application.Json)
            body = jsonBody
        }
    }

    suspend fun transition(issueKey: String, transitionName: String): HttpResponse {
        val transitionIds = getTransitionIds(jiraHttpClient, issueKey)
        val targetTransitionId = transitionIds[transitionName]
            ?: throw RuntimeException("Unknown transition '$transitionName' (from current status)")
        val transitionBody = JsonObject()
        transitionBody.addProperty("id", targetTransitionId)
        val jsonBody = JsonObject()
        jsonBody.add("transition", transitionBody)
        return jiraHttpClient.post {
            url("$baseUrl/rest/api/2/issue/$issueKey/transitions")
            contentType(ContentType.Application.Json)
            body = jsonBody
        }
    }

    private suspend fun getTransitionIds(httpClient: HttpClient, issueKey: String): Map<String, Int> {
        val json = httpClient.get<String> {
            url("${JiraConfig.baseUrl}/rest/api/2/issue/$issueKey/transitions")
        }
        val jsonObject = JsonParser().parse(json).asJsonObject
        val transitionsArray = jsonObject.getAsJsonArray("transitions")
        return transitionsArray
            .map { it.asJsonObject }.map {
                val id = it.getAsJsonPrimitive("id").asInt
                val name = it.getAsJsonPrimitive("name").asString
                name to id
            }
            .toMap()
    }

    private suspend fun getCustomFieldKeys(
        httpClient: HttpClient,
        projectKey: String,
        issueTypeName: String
    ): Map<String, String> {
        val json = httpClient.get<String> {
            url("${JiraConfig.baseUrl}/rest/api/2/issue/createmeta")
            parameter("projectKeys", projectKey)
            parameter("issuetypeNames", issueTypeName)
            parameter("expand", "projects.issuetypes.fields")
        }
        val jsonObject = JsonParser().parse(json).asJsonObject
        return jsonObject
            .get("projects").asJsonArray[0].asJsonObject
            .getAsJsonArray("issuetypes")[0].asJsonObject
            .getAsJsonObject("fields")
            .entrySet()
            .filter { it.key.startsWith("customfield") }
            .map { it.value.asJsonObject.get("name").asString to it.key }
            .toMap()
    }

    private fun createInsightObjectsJsonArray(objectId: Int?): JsonArray {
        val jsonArray = JsonArray()
        if (objectId != null) {
            val jsonObject = JsonObject()
            jsonObject.addProperty("key", objectId.toString())
            jsonArray.add(jsonObject)
        }
        return jsonArray
    }

    private fun createYesNoCustomFieldJson(value: Boolean?): JsonElement =
        if (value == null) {
            JsonNull.INSTANCE
        } else {
            val jsonObject = JsonObject()
            jsonObject.add("value", if (value) JsonPrimitive("yes") else JsonPrimitive("no"))
            jsonObject
        }

    private fun createStringCustomFieldJson(value: String?): JsonElement =
        if (value == null) JsonNull.INSTANCE else JsonPrimitive(value)

    data class LoginRequestBody(val username: String, val password: String)

}