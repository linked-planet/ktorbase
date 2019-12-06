package com.linktime.ktorbase

import com.linktime.ktorbase.model.Session
import com.linktime.ktorbase.routes.health
import com.linktime.ktorbase.routes.index
import com.linktime.ktorbase.routes.saml
import com.linktime.ktorbase.routes.session
import com.linktime.ktorbase.service.SessionService
import com.typesafe.config.ConfigFactory
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.auth.Authentication
import io.ktor.auth.SessionAuthChallenge
import io.ktor.auth.UserIdPrincipal
import io.ktor.auth.session
import io.ktor.client.features.BadResponseStatusException
import io.ktor.features.*
import io.ktor.gson.gson
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.files
import io.ktor.http.content.static
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Locations
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.routing
import io.ktor.sessions.SessionTransportTransformerMessageAuthentication
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.hex


@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.main() {
    install(DefaultHeaders)
    install(CallLogging)
    install(ConditionalHeaders)
    install(Compression)
    install(Locations)
    install(XForwardedHeaderSupport)

    install(StatusPages) {
        exception<NotImplementedError> { call.respond(HttpStatusCode.NotImplemented) }
        exception<BadResponseStatusException> {
            call.response.status(it.statusCode)
            when (it.statusCode.value) {
                HttpStatusCode.Unauthorized.value -> call.respondText("Login failed")
            }
        }
    }

    install(Sessions) {
        val appConfig = ConfigFactory.load().getConfig("ktor.application")
        val secretHashKey = hex(appConfig.getString("secret"))
        cookie<Session>("SESSION") {
            cookie.path = "/"
            transform(SessionTransportTransformerMessageAuthentication(secretHashKey))
        }
    }

    install(Authentication) {
        session<Session> {
            validate {
                val session = it
                SessionService.validateSessionExpiration(it)
                    ?.let { UserIdPrincipal(it.username) }
            }
            challenge = SessionAuthChallenge.Unauthorized
        }
    }

    install(ContentNegotiation) {
        gson {
        }
    }

    routing {
        static("frontend") {
            files("frontend")
        }
        health()
        session()
        index()
        saml()
    }

}
