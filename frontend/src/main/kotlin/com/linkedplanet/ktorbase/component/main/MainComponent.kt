package com.linkedplanet.ktorbase.component.main

import com.linkedplanet.ktorbase.reducers.SessionHandler
import com.linkedplanet.ktorbase.AppState
import com.linkedplanet.ktorbase.component.banner.bannerComponent
import com.linkedplanet.ktorbase.component.login.loginComponent
import com.linkedplanet.ktorbase.component.pages.mainPage.mainPage
import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Notification
import com.linkedplanet.ktorbase.model.NotificationType
import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.reducers.NotificationHandler
import imports.atlaskit.flag.AutoDismissFlag
import imports.atlaskit.flag.FlagGroup
import imports.atlaskit.icon.CheckCircleIcon
import imports.atlaskit.icon.ErrorIcon
import imports.atlaskit.icon.QuestionCircleIcon
import imports.atlaskit.icon.WarningIcon
import kotlinx.html.id
import react.*
import react.dom.div
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction
import kotlin.browser.document
import kotlin.browser.localStorage

interface MainStateProps : RProps {
    var screen: MainComponent.Screen
    var session: Session?
    var config: Config?
    var notifications: List<Notification>
}

interface MainDispatchProps : RProps
interface MainProps : MainStateProps, MainDispatchProps

val mainComponent: RClass<MainProps> =
    rConnect<AppState, RAction, WrapperAction, RProps, MainStateProps, MainDispatchProps, MainProps>(
        { state, _ ->
            screen = state.screen
            session = state.session
            config = state.config
            notifications = state.notifications
        },
        { _, _ -> }
    )(MainComponent::class.js.unsafeCast<RClass<MainProps>>())

class MainComponent(props: MainProps) : RComponent<MainProps, RState>(props) {

    override fun componentDidMount() {
        SessionHandler.currentUser()
    }

    override fun RBuilder.render() {
        localStorage.setItem("theme", "theme-light")
        document.documentElement?.className = "theme-light"
        bannerComponent {}
        div {
            attrs.id = "content"
            props.screen.render(this)
        }
        renderNotifications()
    }

    private fun RBuilder.renderNotifications() =
        FlagGroup {
            attrs.onDismissed = { NotificationHandler.remove(it) }
            props.notifications.forEach { notification ->
                AutoDismissFlag {
                    when (notification.type) {
                        NotificationType.SUCCESS -> attrs.icon = CheckCircleIcon { attrs.primaryColor = "green" }
                        NotificationType.INFO -> attrs.icon = QuestionCircleIcon { attrs.primaryColor = "blue" }
                        NotificationType.WARNING -> attrs.icon = WarningIcon { attrs.primaryColor = "orange" }
                        NotificationType.ERROR -> attrs.icon = ErrorIcon { attrs.primaryColor = "red" }
                    }
                    attrs.title = notification.title
                    attrs.description = notification.description
                }
            }
        }

    enum class Screen {
        Loading {
            override fun render(rb: RBuilder) {
                rb.apply {
                    div {
                        +"Loading ..."
                    }
                }
            }
        },
        Login {
            override fun render(rb: RBuilder) {
                rb.apply {
                    loginComponent {}
                }
            }
        },
        Main {
            override fun render(rb: RBuilder) {
                rb.apply {
                    mainPage {}
                }
            }
        };

        abstract fun render(rb: RBuilder)
    }
}