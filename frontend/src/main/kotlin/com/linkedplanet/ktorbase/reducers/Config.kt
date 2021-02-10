package com.linkedplanet.ktorbase.reducers

import com.linkedplanet.ktorbase.model.Config
import redux.RAction

// ACTIONS
class SaveConfig(val config: Config?): RAction

// reducer
fun config(state: Config? = null, action: RAction): Config? =
    when (action) {
        is SaveConfig -> action.config
        else -> state
    }

// HANDLER
