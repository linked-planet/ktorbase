package com.linkedplanet.ktorbase.component.login

import com.linkedplanet.ktorbase.reducers.SessionHandler
import imports.atlaskit.textfield.Textfield
import kotlinx.html.ButtonType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*
import react.redux.rConnect
import redux.WrapperAction
import kotlin.browser.document
import kotlin.browser.localStorage

val loginComponent: RClass<RProps> =
    rConnect<LoginComponent, WrapperAction>()(LoginComponent::class.js.unsafeCast<RClass<RProps>>())

class LoginComponent : RComponent<RProps, LoginComponent.State>() {

    override fun RBuilder.render() {
        localStorage.setItem("theme", "theme-light")
        document.documentElement?.className = "theme-light"
        div {
            attrs.id = "login-view"
            div {
                attrs.id = "login"
                form {
                    fieldset {
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
                                SessionHandler.login(state.username, state.password)
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
    }

    interface State : RState {
        var disabled: Boolean
        var username: String
        var password: String
        var message: String?
        var error: Boolean
    }

}
