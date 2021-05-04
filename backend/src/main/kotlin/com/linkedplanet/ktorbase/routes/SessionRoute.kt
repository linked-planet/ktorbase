package com.linkedplanet.ktorbase.routes

import com.linkedplanet.ktorbase.config.AppConfig
import com.linkedplanet.ktorbase.model.*
import com.linkedplanet.ktorbase.service.SessionService
import com.linktime.ktor.withSAMLAuth
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import kotlinx.coroutines.*

@KtorExperimentalLocationsAPI
@Location("/session")
object Login

@KtorExperimentalLocationsAPI
@Location("/session")
object Logout

val frontendConfig = Config(
    AppConfig.bannerBackgroundColor,
    AppConfig.bannerMenuBackgroundColor,
    AppConfig.buildVersion
)

@KtorExperimentalLocationsAPI
fun Route.session() {
    post<Login> {
        val loginBody = call.receive<LoginBody>()
        if (loginBody.username != "admin" || loginBody.password != "admin") {
            call.respond(HttpStatusCode.Unauthorized)
            return@post
        }

        val session = SessionService.createSession(loginBody.username)
        call.sessions.set(session)

        call.respond(SessionResponse(session, frontendConfig))
    }

    authenticate {
        get<Login> {
            val session = call.sessions.get<Session>()!!
            SessionService.validateSessionExpiration(session)
                ?.let { SessionService.updateSession(it) }
                ?.apply {
                    call.sessions.set(this)
                    call.respond(SessionResponse(session, frontendConfig))
                    return@get
                }
                ?: apply {
                    call.respond(HttpStatusCode.Unauthorized)
                    return@get
                }

        }
    }

    delete<Logout> {
        if (AppConfig.ssoSaml) {
            withSAMLAuth { auth ->
                call.sessions.clear<Session>()
                withContext(Dispatchers.IO) {
                    auth.logout(null, null, null, true, null)
                }
                call.respond(HttpStatusCode.OK)
            }
        } else {
            call.sessions.clear<Session>()
            call.respond(HttpStatusCode.OK)
        }
    }
}
