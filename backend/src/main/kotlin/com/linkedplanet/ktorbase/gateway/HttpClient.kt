package com.linkedplanet.ktorbase.gateway

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.apache.Apache
import io.ktor.client.engine.apache.ApacheEngineConfig
import io.ktor.client.features.auth.basic.BasicAuth
import io.ktor.client.features.cookies.HttpCookies
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.DEFAULT
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import kotlinx.coroutines.delay
import org.apache.http.impl.NoConnectionReuseStrategy
import org.slf4j.LoggerFactory

fun httpClient(basicUsername: String, basicPassword: String) =
    httpClient {
        it.install(BasicAuth) {
            username = basicUsername
            password = basicPassword
        }
    }

fun httpClient(config: (HttpClientConfig<ApacheEngineConfig>) -> Unit = { Unit }) =
    HttpClient(Apache) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
        install(JsonFeature) {
            serializer = GsonSerializer()
        }
        install(HttpCookies) {}
        config(this)
        engine {
            customizeClient {
                setConnectionReuseStrategy(NoConnectionReuseStrategy())
            }
        }
    }

/**
 * Wraps the creation of the HTTP client so it can be recreated in case it got closed for whatever reason.
 */
class ResilientHttpClient(
    private val name: String,
    private val config: (HttpClientConfig<ApacheEngineConfig>) -> Unit
) {

    private val log = LoggerFactory.getLogger(ResilientHttpClient::class.java)

    private var httpClient = createHttpClient()

    private var httpClientTimestamp: Long = 0

    @Synchronized
    private fun createHttpClient(): HttpClient =
        // we need to prevent recreating the http client over and over in short time via multiple threads
        if ((System.currentTimeMillis() - httpClientTimestamp) > 1000 * 10) {
            log.info("$name: Recreating HTTP client")
            httpClientTimestamp = System.currentTimeMillis()
            httpClient(config)
        } else {
            log.info("$name: Not recreating HTTP client as it is too recent")
            httpClient
        }

    suspend fun <T> withResilience(work: suspend (HttpClient) -> T): T =
        withRetry(2) {
            try {
                work(httpClient)
            } catch (e: Exception) {
                log.error("$name: Handling failure ...", e)
                // recreate the http client so we have the chance to self-heal
                httpClient.close()
                httpClient = createHttpClient()
                // trigger retry
                throw e
            }
        }

    private suspend fun <T> withRetry(maxRetries: Int, attempts: Int = 1, work: suspend () -> T): T =
        try {
            work()
        } catch (e: Exception) {
            log.error("$name: Operation failed (attempt $attempts/$maxRetries)")
            if (attempts == maxRetries) {
                throw RuntimeException("$name: Giving up on operation: (tried $maxRetries times)", e)
            }
            delay(1000)
            withRetry(maxRetries, attempts + 1, work)
        }

}
