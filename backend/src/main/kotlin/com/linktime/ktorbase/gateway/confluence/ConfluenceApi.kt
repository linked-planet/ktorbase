package com.linktime.ktorbase.gateway.confluence

import com.google.gson.JsonParser
import com.linktime.ktorbase.gateway.httpClient
import com.linktime.ktorbase.model.KbPage
import com.linktime.ktorbase.model.KbSearchResult
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

object ConfluenceApi {

    private val baseUrl = ConfluenceConfig.baseUrl

    private val confluenceHttpClient =
        httpClient(ConfluenceConfig.username, ConfluenceConfig.password)

    suspend fun getPage(id: Int): KbPage? {
        val json = confluenceHttpClient.get<String> {
            url("$baseUrl/rest/api/content/$id")
        }
        val jsonObject = JsonParser().parse(json).asJsonObject
        return KbPage(
            id = jsonObject.getAsJsonPrimitive("id").asInt,
            title = jsonObject.getAsJsonPrimitive("title").asString
        )
    }

    suspend fun search(query: String?, includeLabels: Set<String>, excludeLabels: Set<String>): List<KbSearchResult> {
        fun Set<*>.asCqlList(): String =
            joinToString(",") { "\"$it\"" }

        fun Set<*>.asCqlListIfNotEmpty(): String? =
            takeIf { it.isNotEmpty() }?.asCqlList()

        fun String?.expandQuery(parentheses: Boolean = false, op: String, term: String?): String? =
            when {
                this == null -> term
                term == null -> this
                else -> {
                    val (PS, PE) = if (parentheses) "(" to ")" else "" to ""
                    "$PS$this $op $term$PE"
                }
            }

        val siteSearchTerm = if (query.isNullOrBlank()) null else "siteSearch ~ \"$query\""
        val labelIncludeTerm = includeLabels.asCqlListIfNotEmpty()?.let { "label in ($it)" }

        return if (siteSearchTerm == null && labelIncludeTerm == null) {
            emptyList()
        } else {
            val labelExcludeTerm = excludeLabels.asCqlListIfNotEmpty()?.let { "label not in ($it)" }
            val kbSpaceKeys = setOf(ConfluenceConfig.KbConfig.spaceKey)
            val json = confluenceHttpClient.get<String> {
                url("$baseUrl/rest/api/search")
                parameter(
                    "cql", "space in (${kbSpaceKeys.asCqlList()})"
                        .expandQuery(op = "and", term = "type = \"page\"")
                        .expandQuery(op = "and", term = labelExcludeTerm)
                        .expandQuery(
                            op = "and", term =
                            siteSearchTerm.expandQuery(parentheses = true, op = "or", term = labelIncludeTerm)
                        )
                )
                parameter("excerpt", "highlight")
                parameter("start", 0)
                parameter("limit", 100)
                parameter("includeArchivedSpaces", false)
            }
            JsonParser().parse(json)
                .asJsonObject
                .getAsJsonArray("results")
                .map { it.asJsonObject }
                .map {
                    val contentObject = it.getAsJsonObject("content")
                    KbSearchResult(
                        KbPage(
                            id = contentObject.getAsJsonPrimitive("id").asInt,
                            title = contentObject.getAsJsonPrimitive("title").asString
                        ),
                        titleHighlight = it.getAsJsonPrimitive("title").asString,
                        excerptHighlight = it.getAsJsonPrimitive("excerpt").asString
                    )
                }
        }
    }

}