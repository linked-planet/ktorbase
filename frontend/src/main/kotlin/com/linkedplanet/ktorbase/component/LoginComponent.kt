package com.linkedplanet.ktorbase.component

import com.linkedplanet.uikit.wrapper.atlaskit.textfield.TextField
import kotlinx.browser.document
import kotlinx.browser.localStorage
import kotlinx.html.ButtonType
import kotlinx.html.id
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLInputElement
import react.Props
import react.RBuilder
import react.dom.button
import react.dom.div
import react.dom.fieldset
import react.dom.form
import react.dom.legend
import react.fc
import react.useState

external interface LoginProps : Props {
    var login: (String, String) -> Unit
}

private val LoginComponent = fc<LoginProps> { props ->
    val (username, setUsername) = useState("")
    val (password, setPassword) = useState("")

    localStorage.setItem("theme", "theme-light")
    document.documentElement?.className = "theme-light"
    div {
        attrs.id = "login-view"
        div {
            attrs.id = "login"
            form {
                fieldset {
                    legend { +"Login" }
                    TextField {
                        attrs.isCompact = true
                        attrs.placeholder = "Username"
                        attrs.onChange = {
                            val v = (it.target as HTMLInputElement).value
                            setUsername(v)
                        }
                        // TODO add to ui-kit-lib
//                        attrs.autoFocus = true
//                        attrs.autoComplete = "off"
                    }
                    TextField {
                        attrs.isCompact = true
                        attrs.placeholder = "Password"
                        attrs.type = "password"
                        attrs.onChange = {
                            val v = (it.target as HTMLInputElement).value
                            setPassword(v)
                        }
                        // TODO add to ui-kit-lib
//                        attrs.autoComplete = "off"
                    }
                    button(type = ButtonType.submit) {
                        +"Login"
                        attrs.onClickFunction = {
                            it.preventDefault()
                            props.login(username, password)
                        }
                    }
                }
            }
        }
    }
}

fun RBuilder.LoginComponent(handler: LoginProps.() -> Unit) =
    child(LoginComponent) { attrs { handler() } }
