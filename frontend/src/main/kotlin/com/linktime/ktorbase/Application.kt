package com.linktime.ktorbase

import com.linktime.ktorbase.component.banner.bannerComponent
import com.linktime.ktorbase.component.login.loginComponent
import com.linktime.ktorbase.component.main.mainComponent
import com.linktime.ktorbase.model.Config
import com.linktime.ktorbase.model.Session
import com.linktime.ktorbase.request.SessionRequest
import com.linktime.ktorbase.routes.SessionResponse
import com.linktime.ktorbase.store.LogoutAction
import com.linktime.ktorbase.store.SessionAction
import com.linktime.ktorbase.store.SessionStore
import com.linktime.ktorbase.util.Action
import com.linktime.ktorbase.util.Async
import com.linktime.ktorbase.util.Dispatcher
import com.linktime.ktorbase.util.RFluxComponent
import org.w3c.dom.url.URLSearchParams
import react.*
import react.dom.div
import react.dom.render
import kotlin.browser.document
import kotlin.browser.window


class Application : RFluxComponent<Application.Props, Application.State>(
    SessionAction.typeName
) {

    init {
        state.screen = Screen.Loading
    }

    override fun notify(action: Action) {
        when(action) {
            LogoutAction -> loggedOut()
        }
    }

    override fun componentDidMount() {
        Async.complete(
            taskName = "login",
            taskFun = {
                SessionRequest.currentUser()
                    .also { console.log("Login successful: ${it.session.username}") }
            },
            completeFun = { sessionResponse ->
                GlobalOptions.chaosMode = props.chaosMode
                initApp(sessionResponse)
            },
            catchFun = {
                console.log("Login failed")
                setState { screen = Screen.Login }
            }
        )
    }

    private fun initApp(sessionResponse: SessionResponse) {
        Dispatcher.init(
            SessionStore()
        )
        subscribeToActions()
        setState {
            screen = Screen.Main
            session = sessionResponse.session
            config = sessionResponse.config
        }
    }

    private fun loggedOut() {
        setState {
            screen = Screen.Login
            session = null
            config = null
        }
    }

    override fun RBuilder.render() {
        bannerComponent(state.session, state.config, ::loggedOut)
        state.screen.render(state, this)
    }

    enum class Screen {
        Loading {
            override fun render(state: State, rb: RBuilder) {
                rb.apply {
                    div {
                        +"Loading ..."
                    }
                }
            }
        },
        Login {
            override fun render(state: State, rb: RBuilder) {
                rb.apply {
                    loginComponent()
                }
            }
        },
        Main {
            override fun render(state: State, rb: RBuilder) {
                rb.apply {
                    mainComponent(state.config!!)
                }
            }
        };

        abstract fun render(state: State, rb: RBuilder)
    }

    interface Props : RProps {
        var chaosMode: Boolean
    }

    interface State : RState {
        var screen: Screen
        var session: Session?
        var config: Config?
    }

}

fun RBuilder.application(chaosMode: Boolean) = child(Application::class) {
    attrs.chaosMode = chaosMode
}


fun main() {
    // css
    kotlinext.js.require("app.scss")
    kotlinext.js.require("banner.scss")
    kotlinext.js.require("login.scss")

    // favicon
    kotlinext.js.require("favicon.png")

    // main entry point
    render(document.getElementById("content")) {
        val searchParams = URLSearchParams(window.location.search)
        val chaosMode = searchParams.get(PARAMETER_MODE) == VALUE_MODE_CHAOS
        if (chaosMode) {
            console.log("======= CHAOS MODE enabled !!! =======")
            console.log("======= The future is uncertain but the end is always near. =======")
        }
        application(chaosMode)
    }
}

const val PARAMETER_MODE = "mode"
const val VALUE_MODE_CHAOS = "chaos"
