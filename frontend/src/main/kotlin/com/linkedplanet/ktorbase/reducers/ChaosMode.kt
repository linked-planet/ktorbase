package com.linkedplanet.ktorbase.reducers

import com.linkedplanet.ktorbase.appStore
import redux.RAction

// ACTIONS
class SaveChaosMode(val chaosMode: Boolean) : RAction

// reducer
fun chaosMode(state: Boolean = false, action: RAction): Boolean =
    when (action) {
        is SaveChaosMode -> action.chaosMode
        else -> state
    }

// HANDLER
object ChaosModeHandler {

    fun setChaosMode(chaosMode: Boolean) {
        appStore.dispatch(SaveChaosMode(chaosMode))
    }
}


