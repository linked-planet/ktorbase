package com.linktime.ktorbase.component.banner

import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import com.linktime.ktorbase.model.Config
import com.linktime.ktorbase.model.Session
import com.linktime.ktorbase.request.SessionRequest
import com.linktime.ktorbase.util.Async
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.button
import react.dom.div
import react.dom.h1
import react.dom.span
import kotlin.js.json

class BannerComponent : RComponent<BannerComponent.Props, RState>() {

    override fun RBuilder.render() {
        fun RBuilder.buildMenu(config: Config, vararg items: (RBuilder).() -> Unit) {
            div {
                attrs.id = "banner-menu"
                attrs["style"] = json("backgroundColor" to config.bannerMenuBackgroundColor)
                items.withIndex().forEach {
                    if (it.index != 0) {
                        span(classes = "separator") { +"|" }
                    }
                    it.value(this)
                }
            }
        }

        val config = props.config
        val session = props.session
        div {
            attrs.id = "banner"
            if (config != null) {
                attrs["style"] = json("backgroundColor" to config.bannerBackgroundColor)
            }

            div {
                attrs.id = "banner-brand"
                div { attrs.id = "banner-logo" }
                h1 { +"Cockpit" }
            }

            if (config != null && session != null) {
                buildMenu(config,
                    { username(session) },
                    { logoutButton() }
                )
            }
        }
    }

    private fun RBuilder.username(session: Session) {
        span { +session.username }
    }

    private fun RBuilder.logoutButton() {
        button(classes = "button") {
            +"Logout"
            attrs.onClickFunction = { onLogout() }
        }
    }

    private fun onLogout() {
        Async.complete(
            taskName = "logout",
            taskFun = { SessionRequest.logout() },
            completeFun = { props.loggedOut() }
        )
    }

    interface Props : RProps {
        var session: Session?
        var config: Config?
        var loggedOut: () -> Unit
    }

}

fun RBuilder.bannerComponent(session: Session?, config: Config?, loggedOut: () -> Unit) =
    child(BannerComponent::class) {
        attrs.session = session
        attrs.config = config
        attrs.loggedOut = loggedOut
    }
