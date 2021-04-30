package com.linkedplanet.ktorbase.routes

import com.linkedplanet.ktorbase.config.AppConfig
import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.service.SessionService
import com.linktime.ktor.withSAMLAuth
import io.ktor.application.*
import io.ktor.html.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.coroutines.*
import kotlinx.html.*

@KtorExperimentalLocationsAPI
@Location("/")
data class Index(val auth_fallback: Boolean = false)

@KtorExperimentalLocationsAPI
fun Route.index() {
    get<Index> { location ->
        val session = call.sessions.get<Session>()
        val ssoEnabled = AppConfig.ssoSaml && !location.auth_fallback

        if (SessionService.validateSessionExpiration(session) == null && ssoEnabled) {
            withSAMLAuth { withContext(Dispatchers.IO) { it.login() } }
        } else {
            call.respondHtmlTemplate(IndexPage()) {
                caption { +AppConfig.title }
            }
        }
    }
}

class IndexPage : Template<HTML> {
    val caption = Placeholder<TITLE>()

    override fun HTML.apply() {
        head {
            meta { charset = "utf-8" }
            title {
                insert(caption)
            }
            link(href = "frontend/favicon.png", rel = "icon", type = ContentType.Image.PNG.toString()) { }
        }
        body(classes = "app-body") {
            div { id = "content" }
            script(type = "text/javascript", src = "frontend/frontend-${AppConfig.buildVersion}.js", block = {})
        }
    }
}
