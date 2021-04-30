package com.linkedplanet.ktorbase

import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.routes.*
import com.linkedplanet.ktorbase.service.SessionService
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.util.*
import java.util.*


@KtorExperimentalAPI
@KtorExperimentalLocationsAPI
fun Application.main() {

    install(DefaultHeaders)
    install(ConditionalHeaders)
    install(CachingHeaders) {
        options { outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                // Disable cache for index.html so the newest frontend will always be downloaded
                ContentType.Text.Html -> CachingOptions(CacheControl.NoStore(null))
                else -> CachingOptions(CacheControl.NoCache(null))
            }
        }
    }

    install(CallId) {
        generate { UUID.randomUUID().toString() }
    }

    install(CallLogging) {
        mdc("callId") { call ->
            call.callId
        }
        mdc("action") { call ->
            call.request.toLogString()
        }
    }

    install(Compression)
    install(Locations)
    install(XForwardedHeaderSupport)

    install(StatusPages) {
        exception<NotImplementedError> { call.respond(HttpStatusCode.NotImplemented) }
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
            validate { session ->
                SessionService.validateSessionExpiration(session)
                    ?.let { UserIdPrincipal(it.username) }
            }
            challenge {
                call.respond(UnauthorizedResponse())
            }
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
