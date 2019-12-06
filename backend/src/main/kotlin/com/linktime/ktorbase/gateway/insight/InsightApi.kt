package com.linktime.ktorbase.gateway.insight

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.linktime.ktorbase.gateway.httpClient
import com.linktime.ktorbase.gateway.jira.JiraConfig
import com.linktime.ktorbase.model.Service

import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

object InsightApi {

    private val baseUrl = JiraConfig.baseUrl

    private val jiraHttpClient =
        httpClient(JiraConfig.username, JiraConfig.password)

    suspend fun getActiveServices(): List<Service> {
        val json = jiraHttpClient.get<String> {
            url("$baseUrl/rest/insight-object-graph/1.0/objects/${ObjectTypes.Service}")
            parameter("schemaId", InsightConfig.schemaId)
            parameter("attributes", CommonAttributes.name.name)
            parameter("attributes", ServiceAttributes.status.name)
        }
        val responseArray = JsonParser().parse(json).asJsonArray
        val allObjects = responseArray.map { it.asJsonObject }.map { jsonObject ->
            val id = jsonObject.getAsJsonPrimitive("id").asInt
            val name = CommonAttributes.name.requireAttributeValue(jsonObject)
            val status = ServiceAttributes.status.getValueOrNull(jsonObject)
            Service(id, name, status)
        }
        return allObjects.filter {
            it.status in setOf(
                ServiceAttributes.StatusActive
            )
        }
    }

}

// -------------------------------------------------------------------------------------------
// Insight Attribute Parsing based on Type
// -------------------------------------------------------------------------------------------

interface InsightAttribute {
    val name: String

    fun getAttributeArray(jsonObject: JsonObject): JsonArray? {
        val attributes = jsonObject.getAsJsonArray("attributes").map { it.asJsonObject }
        val attributeObject = attributes.find {
            it.getAsJsonPrimitive("name").asString == name
        }
        return attributeObject?.getAsJsonArray("values")
    }
}

abstract class SingleValueInsightAttribute<T>(private val valueCast: (JsonElement) -> T) : InsightAttribute {
    fun requireAttributeValue(jsonObject: JsonObject): T =
        valueCast(getAttributeArray(jsonObject)!!.first())

    fun getValueOrNull(jsonObject: JsonObject): T? {
        val value = getAttributeArray(jsonObject)?.firstOrNull()
        return value?.let { valueCast(it) }
    }
}

class MultiValueInsightAttribute<T>(override val name: String, private val valueCast: (JsonElement) -> T) :
    InsightAttribute {
    fun getValueOrNull(jsonObject: JsonObject): List<T>? =
        getAttributeArray(jsonObject)?.map { valueCast(it) }
}

class StringInsightAttribute(override val name: String) : SingleValueInsightAttribute<String>(JsonElement::getAsString)
class IntInsightAttribute(override val name: String) : SingleValueInsightAttribute<Int>(JsonElement::getAsInt)
class DoubleInsightAttribute(override val name: String) : SingleValueInsightAttribute<Double>(JsonElement::getAsDouble)

// -------------------------------------------------------------------------------------------
// List of used attributes
// -------------------------------------------------------------------------------------------

object CommonAttributes {
    val name = StringInsightAttribute("Name")
}

object ServiceAttributes {
    val status = StringInsightAttribute("Status")
    const val StatusActive = "Active"
}

object ObjectTypes {
    const val Service: String = "Service"
}
