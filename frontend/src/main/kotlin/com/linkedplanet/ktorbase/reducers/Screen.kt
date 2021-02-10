package com.linkedplanet.ktorbase.reducers

import com.linkedplanet.ktorbase.component.main.MainComponent
import redux.RAction

// ACTIONS
class UpdateScreenAction(val screen: MainComponent.Screen): RAction

// reducer
fun screen(state: MainComponent.Screen = MainComponent.Screen.Loading, action: RAction): MainComponent.Screen =
    when (action) {
        is UpdateScreenAction -> action.screen
        else -> state
    }

// HANDLER
