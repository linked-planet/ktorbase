package com.linktime.ktorbase.routes

import com.linktime.ktor.withSAMLAuth
import com.linktime.ktorbase.model.Config
import com.linktime.ktorbase.model.Session
import com.linktime.ktorbase.service.SessionService
import io.ktor.application.call
import io.ktor.auth.authenticate
import io.ktor.http.HttpStatusCode
import io.ktor.locations.*
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.sessions.clear
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@KtorExperimentalLocationsAPI
@Location("/session")
object Login

@KtorExperimentalLocationsAPI
@Location("/session")
object Logout

val frontendConfig = Config(
    com.linktime.ktorbase.AppConfig.bannerBackgroundColor,
    com.linktime.ktorbase.AppConfig.bannerMenuBackgroundColor
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
                    call.sessions.set (this)
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
        if (com.linktime.ktorbase.AppConfig.ssoSaml) {
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