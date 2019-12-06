package com.linktime.ktorbase.routes

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.locations.KtorExperimentalLocationsAPI
import io.ktor.locations.Location
import io.ktor.locations.get
import io.ktor.response.respond
import io.ktor.routing.Route

@KtorExperimentalLocationsAPI
@Location("/health")
class Health

@KtorExperimentalLocationsAPI
fun Route.health() {
    get<Health> {
        call.respond(HttpStatusCode.OK, "cockpit: ok")
    }
}
