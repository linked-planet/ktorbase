package com.linktime.ktorbase.util

import com.linktime.ktorbase.DevOptions
import com.linktime.ktorbase.GlobalOptions
import kotlinx.coroutines.await
import org.w3c.fetch.*
import kotlin.browser.window
import kotlin.js.json
import kotlin.random.Random

suspend fun <T> requestAndParseNullableResult(
    url: String,
    method: String = "GET",
    headers: dynamic = json(),
    requestCredentials: RequestCredentials = RequestCredentials.SAME_ORIGIN,
    requestRedirect: RequestRedirect = RequestRedirect.FOLLOW,
    body: dynamic = null,
    parse: (dynamic) -> T
): T? =
    requestAndHandle(
        url = url,
        method = method,
        headers = headers,
        requestCredentials = requestCredentials,
        requestRedirect = requestRedirect,
        body = body,
        handler = { response ->
            when {
                response.status.toInt() < 400 -> parse(response.json().await())
                response.status.toInt() == 404 -> null
                else -> throw BadStatusCodeException(response.status.toInt())
            }
        }
    )

suspend fun <T> requestAndParseResult(
    url: String,
    method: String = "GET",
    headers: dynamic = json(),
    requestCredentials: RequestCredentials = RequestCredentials.SAME_ORIGIN,
    requestRedirect: RequestRedirect = RequestRedirect.FOLLOW,
    body: dynamic = null,
    parse: (dynamic) -> T
): T =
    requestAndHandle(
        url = url,
        method = method,
        headers = headers,
        requestCredentials = requestCredentials,
        requestRedirect = requestRedirect,
        body = body,
        handler = { response ->
            if (response.status < 400) {
                val json = response.json().await()
                parse(json)
            } else {
                throw BadStatusCodeException(response.status.toInt())
            }
        }
    )

suspend fun <T> requestAndHandleSuccess(
    url: String,
    method: String = "GET",
    headers: dynamic = json(),
    requestCredentials: RequestCredentials = RequestCredentials.SAME_ORIGIN,
    requestRedirect: RequestRedirect = RequestRedirect.FOLLOW,
    body: dynamic = null,
    handler: suspend (Response) -> T
): T =
    requestAndHandle(
        url = url,
        method = method,
        headers = headers,
        requestCredentials = requestCredentials,
        requestRedirect = requestRedirect,
        body = body,
        handler = { response ->
            if (response.status < 400) {
                handler(response)
            } else {
                throw BadStatusCodeException(response.status.toInt())
            }
        }
    )

suspend fun <T> requestAndHandle(
    url: String,
    method: String = "GET",
    headers: dynamic = json(),
    requestCredentials: RequestCredentials = RequestCredentials.SAME_ORIGIN,
    requestRedirect: RequestRedirect = RequestRedirect.FOLLOW,
    body: dynamic = null,
    handler: suspend (Response) -> T
): T {
    DevOptions.RandomHttpDelay.randomDelay()
    DevOptions.FailSpecificHttpRequests.failIfDesired(method, url)
    val chaosCausedFailure = GlobalOptions.chaosMode && Random.nextBoolean()
    val response = if (chaosCausedFailure) {
        console.log("CHAOS: $method - $url")
        throw ChaosModeException()
    } else {
        window.fetch(url, object : RequestInit {
            override var method: String? = method
            override var headers: dynamic = headers
            override var credentials: RequestCredentials? = requestCredentials
            override var redirect: RequestRedirect? = requestRedirect
            override var body: dynamic = body
        }).await()
    }
    return handler(response)
}

class BadStatusCodeException(val statusCode: Int) : RuntimeException()
class ChaosModeException : RuntimeException()