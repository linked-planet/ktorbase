package com.linkedplanet.ktorbase.routes

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.util.pipeline.PipelineContext

val Int.httpSuccess: Boolean
    get() = this in 200..299

suspend fun <T> ApplicationCall.respondNotFoundIfNull(value: T?, handler: suspend (T) -> Unit) {
    if (value == null) respond(HttpStatusCode.NotFound) else handler(value)
}
