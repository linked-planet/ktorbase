package com.linkedplanet.ktorbase

object GlobalOptions {

    /** how many millis to wait before triggering actions as reaction to user input */
    const val inputActionDelay: Int = 1000

    /** causes HTTP requests to fail randomly, intended for testing error handling functionality */
    var chaosMode: Boolean = false

}