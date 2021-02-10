package com.linkedplanet.ktorbase.gateway.confluence

import com.google.gson.JsonParser
import com.linkedplanet.ktorbase.gateway.httpClient
import com.linkedplanet.ktorbase.model.Language
import io.ktor.client.request.get
import io.ktor.client.request.url

object ScrollTranslationsApi {

    private val baseUrl = ConfluenceConfig.baseUrl

    private const val basePath = "rest/scroll-versions/1.0"

    private fun confluenceHttpClient() =
        httpClient(ConfluenceConfig.username, ConfluenceConfig.password)

    suspend fun getLanguages(): List<Language> =
        confluenceHttpClient().use { client ->
            val json = client.get<String> {
                url("$baseUrl/$basePath/translation/${ConfluenceConfig.KbConfig.homePageId}")
            }
            val jsonVersions = JsonParser().parse(json).asJsonArray
            jsonVersions.map { it.asJsonObject }.map { versionObject ->
                val languageObject = versionObject.getAsJsonObject("language")
                val code = languageObject.getAsJsonPrimitive("key").asString
                val displayName = languageObject.getAsJsonPrimitive("displayName").asString
                Language(code, displayName)
            }
        }

}