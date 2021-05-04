package com.linkedplanet.ktorbase

import com.linkedplanet.ktorbase.component.main.mainComponent
import com.linkedplanet.ktorbase.reducers.ChaosModeHandler
import kotlinx.browser.*
import org.w3c.dom.url.URLSearchParams
import react.*
import react.dom.render
import react.redux.*
import redux.*

val appStore = createStore<AppState, RAction, dynamic>(
    appReducers(), AppState(), compose(
        rEnhancer(),
        js("if(window.__REDUX_DEVTOOLS_EXTENSION__ )window.__REDUX_DEVTOOLS_EXTENSION__ ();else(function(f){return f;});")
    )
)

val application: RClass<RProps> =
    rConnect<Application, WrapperAction>()(Application::class.js.unsafeCast<RClass<RProps>>())

class Application : RComponent<RProps, RState>() {
    override fun RBuilder.render() {
        mainComponent {}
    }
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
        provider(appStore) {
            ChaosModeHandler.setChaosMode(chaosMode)
            application {}
        }
    }
}

const val PARAMETER_MODE = "mode"
const val VALUE_MODE_CHAOS = "chaos"
