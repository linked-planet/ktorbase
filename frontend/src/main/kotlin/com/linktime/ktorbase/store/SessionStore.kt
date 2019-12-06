package com.linktime.ktorbase.store

import com.linktime.ktorbase.util.Action
import com.linktime.ktorbase.util.FluxStore
import com.linktime.ktorbase.util.RFluxComponent


abstract class SessionAction : Action {
    override val typeName: String = SessionAction.typeName

    companion object {
        val typeName: String = SessionAction::class.simpleName!!
    }
}

object LogoutAction : SessionAction()

class SessionStore : FluxStore(SessionAction.typeName) {

    override fun init(component: RFluxComponent<*, *>) {
    }

    override fun accept(action: Action) {
        when (action) {
            LogoutAction -> {
                notify(action)
            }
        }
    }

}