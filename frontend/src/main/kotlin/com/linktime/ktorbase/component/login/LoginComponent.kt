package com.linktime.ktorbase.component.login

import atlaskit.textfield.Textfield
import com.linktime.ktorbase.request.SessionRequest
import com.linktime.ktorbase.util.Async
import kotlinx.html.ButtonType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.dom.span
import kotlin.browser.window

class LoginComponent : RComponent<RProps, LoginComponent.State>() {

    override fun RBuilder.render() {
        div {
            attrs.id = "login"
            form {
                fieldSet {
                    legend { +"Login" }
                    attrs.disabled = state.disabled
                    Textfield {
                        attrs.isCompact = true
                        attrs.placeholder = "Username"
                        attrs.onChange = {
                            val v = (it.target as HTMLInputElement).value
                            setState { username = v }
                        }
                        attrs.autoFocus = true
                        attrs.autoComplete = "off"
                    }
                    Textfield {
                        attrs.isCompact = true
                        attrs.placeholder = "Password"
                        attrs.type = "password"
                        attrs.onChange = {
                            val v = (it.target as HTMLInputElement).value
                            setState { password = v }
                        }
                        attrs.autoComplete = "off"
                    }
                    button(type = ButtonType.submit) {
                        +"Login"
                        attrs.disabled = state.disabled
                        attrs.onClickFunction = {
                            it.preventDefault()
                            doLogin()
                        }
                    }
                    state.message?.takeIf { it.isNotEmpty() }?.let { message ->
                        val classes = if (state.error) "error" else ""
                        span(classes) { +message }
                    }
                }
            }
        }
    }

    private fun doLogin() {
        setState {
            disabled = true
            message = "..."
            error = false
        }
        Async.complete(
            taskName = "login",
            taskFun = { SessionRequest.login(state.username, state.password) },
            completeFun = {
                if (it == null) {
                    loginFailed("Login Failure")
                } else {
                    window.location.reload()
                }
            },
            catchFun = { loginFailed("Login Failure") }
        )
    }

    private fun loginFailed(errorMessage: String) {
        setState {
            disabled = false
            message = errorMessage
            error = true
        }
    }

    interface State : RState {
        var disabled: Boolean
        var username: String
        var password: String
        var message: String?
        var error: Boolean
    }

}

fun RBuilder.loginComponent() = child(LoginComponent::class) {}
