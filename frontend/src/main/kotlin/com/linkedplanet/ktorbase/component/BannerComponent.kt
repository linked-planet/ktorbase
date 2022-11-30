package com.linkedplanet.ktorbase.component

import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.uikit.util.createElementNullSafe
import com.linkedplanet.uikit.wrapper.atlaskit.navigation.AtlassianNavigation
import com.linkedplanet.uikit.wrapper.atlaskit.navigation.CustomProductHome
import com.linkedplanet.uikit.wrapper.atlaskit.navigation.Profile
import com.linkedplanet.uikit.wrapper.atlaskit.popup.Popup
import imports.atlaskit.menu.LinkItem
import imports.atlaskit.menu.MenuGroup
import kotlinx.html.id
import kotlinx.js.Object
import kotlinx.js.jso
import react.Props
import react.RBuilder
import react.createElement
import react.dom.div
import react.dom.img
import react.dom.jsStyle
import react.fc
import react.useState

external interface BannerComponentProps : Props {
    var session: Session?
    var config: Config?
    var logout: () -> Unit
}

private val BannerComponent = fc<BannerComponentProps> { props ->
    val (profilePopupIsOpen, setProfilePopupIsOpen) = useState(false)
    val config = props.config
    val session = props.session
    div {
        attrs.id = "banner"
        AtlassianNavigation {
            attrs.renderProductHome = {
                createElement(CustomProductHome, jso {
                    iconUrl = "frontend/favicon.png"
                    logoUrl = "frontend/favicon.png"
                    siteTitle = "KtorBase"
                })
            }
            if (config != null && session != null) {
                val profileIcon = createElementNullSafe {
                    img {
                        attrs.src =
                            "https://w7.pngwing.com/pngs/7/618/png-transparent-man-illustration-avatar-icon-fashion-men-avatar-face-fashion-girl-heroes.png"
                        attrs.jsStyle {
                            borderRadius = "50%"
                            width = 32
                            height = 32
                        }
                    }
                }
                attrs.renderProfile = {
                    createElement(Popup, jso {
                        isOpen = profilePopupIsOpen
                        onClose = { setProfilePopupIsOpen(false) }
                        placement = "bottom-start"
                        trigger = { triggerProps ->
                            createElementNullSafe {
                                Profile {
                                    Object.keys(triggerProps).forEach { key ->
                                        val descriptor = Object.getOwnPropertyDescriptor<Props>(triggerProps, key)
                                        Object.defineProperty(attrs, key, descriptor)
                                    }
                                    attrs.icon = profileIcon
                                    attrs.onClick = { setProfilePopupIsOpen(!profilePopupIsOpen) }
                                }
                            }
                        }
                        content = { _ ->
                            createElementNullSafe {
                                MenuGroup {
                                    LinkItem {
                                        +"Logout"
                                        attrs.onClick = {
                                            setProfilePopupIsOpen(false)
                                            props.logout()
                                        }
                                    }
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}

fun RBuilder.BannerComponent(handler: BannerComponentProps.() -> Unit) =
    child(BannerComponent) { attrs { handler() } }
