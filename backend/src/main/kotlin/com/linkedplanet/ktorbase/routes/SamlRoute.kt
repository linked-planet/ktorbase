package com.linkedplanet.ktorbase.routes

import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.service.SessionService
import com.linktime.ktor.SamlConfig
import com.linktime.ktor.getServletRequest
import com.linktime.ktor.requireValid
import com.linktime.ktor.withSAMLAuth
import com.onelogin.saml2.Auth
import com.onelogin.saml2.settings.Saml2Settings
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.html.respondHtml
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.locations.post
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.Route
import io.ktor.sessions.clear
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.pipeline.PipelineContext
import kotlinx.html.body
import kotlinx.html.li
import kotlinx.html.p
import kotlinx.html.ul


const val SAMLEndpointBasePath: String = "/sso/saml"

@KtorExperimentalLocationsAPI
@Location("$SAMLEndpointBasePath/metadata")
class Metadata

@KtorExperimentalLocationsAPI
@Location("$SAMLEndpointBasePath/acs")
class AttributeConsumerService

@KtorExperimentalLocationsAPI
@Location("$SAMLEndpointBasePath/sls")
class SingleLogoutService

@KtorExperimentalLocationsAPI
fun Route.saml() {
    suspend fun PipelineContext<Unit, ApplicationCall>.requireValid(errors: List<String>, handler: suspend () -> Unit) {
        requireValid(errors, handler) { _ ->
            call.application.environment.log.error(errors.joinToString())
            call.respondHtml(HttpStatusCode.BadRequest) {
                body {
                    ul {
                        errors.forEach {
                            li { +it }
                        }
                    }
                }
            }
        }
    }

    suspend fun PipelineContext<Unit, ApplicationCall>.requireValid(auth: Auth, handler: suspend () -> Unit) {
        requireValid(auth, handler) { errors ->
            call.application.environment.log.error(errors.joinToString())
            call.respondHtml(HttpStatusCode.BadRequest) {
                body {
                    val lastErrorReason = auth.lastErrorReason
                    if (auth.isDebugActive && !lastErrorReason.isNullOrBlank()) {
                        p { +lastErrorReason }
                    }
                    ul {
                        errors.forEach {
                            li { +it }
                        }
                    }
                }
            }
        }
    }

    get<Metadata> {
        requireSAMLEnabled {
            val settings = SamlConfig.saml2Settings
            val metadata = settings.spMetadata
            val errors = Saml2Settings.validateMetadata(metadata)
            requireValid(errors) {
                call.respond(metadata)
            }
        }
    }

    post<AttributeConsumerService> {
        requireSAMLEnabled {
            withSAMLAuth { auth ->
                // saml auth / ktor "consume" the form parameters so we won't be able to get the relay state anymore
                val servletRequest = call.getServletRequest()
                val relayState = servletRequest.getParameter("RelayState")
                call.application.environment.log.debug("RelayState: $relayState")

                auth.processResponse()
                requireValid(auth) {
                    if (!auth.isAuthenticated) {
                        call.respond(HttpStatusCode.Unauthorized, "Not authenticated")
                    } else {
                        val nameId = auth.nameId
                        val session = SessionService.createSession(nameId)
                        call.sessions.set(session)

                        if (relayState != null) {
                            call.respondRedirect(relayState)
                        } else {
                            call.respondRedirect { encodedPath = "/" }
                        }
                    }
                }
            }
        }
    }

    get<SingleLogoutService> {
        requireSAMLEnabled {
            withSAMLAuth { auth ->
                // SLORequest: Will validate and redirect to IdP
                // SLOResponse: Will validate and clear session
                // In any case, keepLocalSession as the library's way of clearing the session is incompatible with ktor
                auth.processSLO(true, null)
                // in case of SLOResponse, we are still here
                requireValid(auth) {
                    call.sessions.clear<Session>()
                    call.respondRedirect { encodedPath = "/" }
                }
            }
        }
    }

}

suspend fun PipelineContext<Unit, ApplicationCall>.requireSAMLEnabled(handler: suspend () -> Unit) {
    if (!com.linkedplanet.ktorbase.AppConfig.ssoSaml) call.respond(HttpStatusCode.BadRequest) else handler()
}