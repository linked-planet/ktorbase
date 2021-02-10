package com.linkedplanet.ktorbase

import com.linkedplanet.ktorbase.component.main.MainComponent
import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Notification
import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.reducers.*
import redux.Reducer
import redux.combineReducers
import kotlin.reflect.KProperty1

data class AppState(
    val notifications: List<Notification> = emptyList(),
    val screen: MainComponent.Screen = MainComponent.Screen.Loading,
    val session: Session? = null,
    val config: Config? = null,
    val chaosMode: Boolean = false
)


fun <S, A, R> combinePropertyReducers(reducers: Map<KProperty1<S, R>, Reducer<*, A>>): Reducer<S, A> {
    return combineReducers(reducers.mapKeys { it.key.name })
}

fun appReducers() = combinePropertyReducers(
    mapOf(
        AppState::notifications to ::notifications,
        AppState::screen to ::screen,
        AppState::session to ::session,
        AppState::config to ::config,
        AppState::chaosMode to ::chaosMode
    )
)