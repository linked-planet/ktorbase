package com.linkedplanet.ktorbase.reducers

import com.linkedplanet.ktorbase.appStore
import com.linkedplanet.ktorbase.component.main.MainComponent
import com.linkedplanet.ktorbase.model.*
import com.linkedplanet.ktorbase.request.SessionRequest
import com.linkedplanet.ktorbase.util.Async
import kotlinx.browser.localStorage
import redux.RAction

// ACTIONS
class SaveSessionAction(val session: Session?) : RAction

// reducer
fun session(state: Session? = null, action: RAction): Session? =
    when (action) {
        is SaveSessionAction -> action.session
        else -> state
    }

// HANDLER
object SessionHandler {

    fun login(username: String, password: String) {
        Async.complete(
            taskName = "login",
            taskFun = { SessionRequest.login(username, password) },
            completeFun = { sessionResponse ->
                if (sessionResponse == null) {
                    loginFailed()
                } else {
                    currentUser()
                }
            },
            catchFun = { loginFailed() }
        )
    }

    fun currentUser() {
        Async.complete(
            taskName = "login",
            taskFun = {
                SessionRequest.currentUser()
                    .also { console.log("Login successful: ${it.session.username}") }
            },
            completeFun = { sessionResponse ->
                appStore.dispatch(SaveSessionAction(sessionResponse.session))
                appStore.dispatch(SaveConfig(sessionResponse.config))
                appStore.dispatch(UpdateScreenAction(MainComponent.Screen.Main))
                undefined
            },
            catchFun = {
                appStore.dispatch(UpdateScreenAction(MainComponent.Screen.Login))
                undefined
            }
        )
    }

    fun logout() {
        Async.complete(
            taskName = "logout",
            taskFun = { SessionRequest.logout() },
            completeFun = {
                appStore.dispatch(SaveSessionAction(null))
                appStore.dispatch(SaveConfig(null))
                appStore.dispatch(UpdateScreenAction(MainComponent.Screen.Login))
                localStorage.clear()
            }
        )
    }

    private fun loginFailed() {
        console.log("Login Failure")
        NotificationHandler.show(NotificationType.ERROR, "Login failed", "Please try again.")
        appStore.dispatch(UpdateScreenAction(MainComponent.Screen.Login))
        localStorage.clear()
    }
}
