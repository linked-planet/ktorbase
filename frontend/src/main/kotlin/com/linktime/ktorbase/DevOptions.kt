package com.linktime.ktorbase

import kotlinx.coroutines.delay
import kotlin.random.Random

@Suppress("ConstantConditionIf")
object DevOptions {

    abstract class DevOption(protected val enabled: Boolean) {
        init {
            if (enabled) {
                console.log("======= ATTENTION: ${this::class.simpleName} developer option is enabled !!! =======")
                console.log("======= If this is production, re-build the application without the flag =======")
            }
        }
    }

    object FailSpecificHttpRequests : DevOption(false) {
        private val failRoutes: List<FailRoute> = listOf(
        )

        suspend fun failIfDesired(method: String, path: String) {
            if (enabled) {
                failRoutes
                    .find { it.method == method && it.pathRegex.containsMatchIn(path) }
                    ?.let { route ->
                        console.log("${this::class.simpleName} --> $route")
                        delay(500)
                        throw FailSpecificHttpRequestException()
                    }
            }
        }

        data class FailRoute(val method: String, val pathRegex: Regex)

        class FailSpecificHttpRequestException : RuntimeException()
    }

    object RandomHttpDelay : DevOption(false) {
        private const val maxDelayMillis = 1500L

        suspend fun randomDelay() {
            if (enabled) {
                val delayMillis = Random.nextLong(maxDelayMillis)
                console.log("${this::class.simpleName} --> $delayMillis ms")
                delay(delayMillis)
            }
        }
    }

}