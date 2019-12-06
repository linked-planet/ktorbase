package com.linktime.ktorbase.request

fun createUrlParameters(vararg values: Pair<String, Any?>): String =
    values.joinToString(separator = "&", prefix = "?") {
        val name = it.first
        val value = it.second?.toString()?.encodeUriComponent() ?: ""
        "$name=$value"
    }

private fun String?.encodeUriComponent(): String = this?.let { encodeURIComponent(it) } ?: ""

private external fun encodeURIComponent(str: String): String