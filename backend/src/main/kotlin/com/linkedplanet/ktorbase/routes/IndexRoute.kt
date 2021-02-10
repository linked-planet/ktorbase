package com.linkedplanet.ktorbase.routes

import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.service.SessionService
import com.linktime.ktor.withSAMLAuth
import io.ktor.application.call
import io.ktor.html.Placeholder
import io.ktor.html.Template
import io.ktor.html.insert
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.routing.Route
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.util.InternalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.html.*

@KtorExperimentalLocationsAPI
@Location("/")
data class Index(val auth_fallback: Boolean = false)

@UseExperimental(InternalAPI::class)
@KtorExperimentalLocationsAPI
fun Route.index() {
    get<Index> { location ->
        val session = call.sessions.get<Session>()
        val ssoEnabled = com.linkedplanet.ktorbase.AppConfig.ssoSaml && !location.auth_fallback

        if (SessionService.validateSessionExpiration(session) == null && ssoEnabled) {
            withSAMLAuth { withContext(Dispatchers.IO) { it.login() } }
        } else {
            call.respondHtmlTemplate(IndexPage()) {
                caption { +com.linkedplanet.ktorbase.AppConfig.title }
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
            script(type = "text/javascript", src = "frontend/frontend.bundle.js", block = {})
        }
    }
}
