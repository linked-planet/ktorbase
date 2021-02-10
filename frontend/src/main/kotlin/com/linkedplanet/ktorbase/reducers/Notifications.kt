package com.linkedplanet.ktorbase.reducers

import com.linkedplanet.ktorbase.appStore
import com.linkedplanet.ktorbase.model.Notification
import com.linkedplanet.ktorbase.model.NotificationType
import redux.RAction

// ACTIONS
class AddNotification(val notification: Notification) : RAction
class RemoveNotification(val string: String) : RAction

// reducer
fun notifications(state: List<Notification> = emptyList(), action: RAction): List<Notification> =
    when (action) {
        is AddNotification -> state + action.notification
        is RemoveNotification -> state.drop(1)
        else -> state
    }

// HANDLER
object NotificationHandler{

    fun show(type: NotificationType, title: String, description: String) {
        appStore.dispatch(AddNotification(Notification(type, title, description)))
    }

    fun remove(string: String) {
        appStore.dispatch(RemoveNotification(string))
    }
}


