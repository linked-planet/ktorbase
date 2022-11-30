package com.linkedplanet.ktorbase.component.main

import com.linkedplanet.ktorbase.component.banner.BannerComponent
import com.linkedplanet.ktorbase.component.login.LoginComponent
import com.linkedplanet.ktorbase.component.pages.mainPage.MainPage
import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Notification
import com.linkedplanet.ktorbase.model.NotificationType
import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.request.SessionRequest
import com.linkedplanet.uikit.util.Async
import com.linkedplanet.uikit.wrapper.atlaskit.flag.AutoDismissFlag
import com.linkedplanet.uikit.wrapper.atlaskit.flag.FlagGroup
import com.linkedplanet.uikit.wrapper.atlaskit.icon.CheckCircleIcon
import com.linkedplanet.uikit.wrapper.atlaskit.icon.ErrorIcon
import com.linkedplanet.uikit.wrapper.atlaskit.icon.QuestionCircleIcon
import com.linkedplanet.uikit.wrapper.atlaskit.icon.WarningIcon
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.html.id
import react.Props
import react.RBuilder
import react.dom.div
import react.fc
import react.useEffect
import react.useState

private val MainComponent = fc<Props> { _ ->
    val (appState, setAppState) = useState<AppState>(AppState.Loading)
    val (notifications, setNotifications) = useState(emptyList<Notification>())

    fun getCurrentUser() {
        Async.complete(
            taskName = "login",
            taskFun = {
                SessionRequest.currentUser()
                    .also { console.log("Login successful: ${it.session.username}") }
            },
            completeFun = { sessionResponse ->
                setAppState(AppState.Main(sessionResponse.session, sessionResponse.config))
            },
            catchFun = {
                setAppState(AppState.Login)
            }
        )
    }

    fun login(username: String, password: String) {
        fun loginFailed() {
            console.log("Login Failure")
            setNotifications(notifications + Notification(NotificationType.ERROR, "Login failed", "Please try again."))
            setAppState(AppState.Login)
            localStorage.clear()
        }

        Async.complete(
            taskName = "login",
            taskFun = { SessionRequest.login(username, password) },
            completeFun = { sessionResponse ->
                if (sessionResponse == null) loginFailed()
                else getCurrentUser()
            },
            catchFun = { loginFailed() }
        )
    }

    fun logout() {
        Async.complete(
            taskName = "logout",
            taskFun = { SessionRequest.logout() },
            completeFun = {
                setAppState(AppState.Login)
                localStorage.clear()
            }
        )
    }

    useEffect(*emptyArray()) {
        getCurrentUser()
    }

    localStorage.setItem("theme", "theme-light")
    document.documentElement?.className = "theme-light"
    BannerComponent {
        this.session = if (appState is AppState.Main) appState.session else null
        this.config = if (appState is AppState.Main) appState.config else null
        this.logout = ::logout
    }
    div {
        attrs.id = "content"
        when (appState) {
            AppState.Loading ->
                div {
                    +"Loading ..."
                }

            AppState.Login ->
                LoginComponent {
                    this.login = ::login
                }

            is AppState.Main -> {
                MainPage {
                    this.session = appState.session
                    this.config = appState.config
                }
            }
        }
    }
    renderNotifications(
        notifications,
        removeLastNotification = { setNotifications(notifications.drop(1)) }
    )
}

private fun RBuilder.renderNotifications(
    notifications: List<Notification>,
    removeLastNotification: () -> Unit
) =
    FlagGroup {
        attrs.onDismissed = { removeLastNotification() }
        notifications.forEach { notification ->
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

sealed interface AppState {
    object Loading : AppState
    object Login : AppState
    class Main(val session: Session, val config: Config) : AppState
}

fun RBuilder.MainComponent(handler: Props.() -> Unit) =
    child(MainComponent) { attrs { handler() } }
