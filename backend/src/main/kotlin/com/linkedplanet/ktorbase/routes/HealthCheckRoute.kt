package com.linkedplanet.ktorbase.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*
import io.ktor.routing.*

@KtorExperimentalLocationsAPI
@Location("/health")
class Health

@KtorExperimentalLocationsAPI
fun Route.health() {
    get<Health> {
        call.respond(HttpStatusCode.OK, "service: ok")
    }
}
