package com.linkedplanet.ktorbase.component.banner

import com.linkedplanet.ktorbase.reducers.SessionHandler
import com.linkedplanet.ktorbase.AppState
import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Session
import imports.atlaskit.menu.LinkItem
import imports.atlaskit.menu.MenuGroup
import imports.atlaskit.navigation.AtlassianNavigation
import imports.atlaskit.navigation.CustomProductHome
import imports.atlaskit.navigation.Profile
import imports.atlaskit.popup.Popup
import kotlinext.js.Object
import kotlinx.html.id
import react.*
import react.dom.div
import react.dom.img
import react.dom.jsStyle
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction

interface BannerStateProps : RProps {
    var session: Session?
    var config: Config?
}

interface BannerDispatchProps : RProps
interface BannerProps : BannerStateProps, BannerDispatchProps

val bannerComponent: RClass<BannerProps> =
    (rConnect<AppState, RAction, WrapperAction, RProps, BannerStateProps, BannerDispatchProps, BannerProps>(
        { state, _ ->
            session = state.session
            config = state.config
        },
        { _, _ -> }
    ))(BannerComponent::class.js.unsafeCast<RClass<BannerProps>>())

class BannerComponent(props: BannerProps) : RComponent<BannerProps, BannerComponent.State>(props) {

    init {
        state.profilePopupIsOpen = false
    }

    override fun RBuilder.render() {
        val config = props.config
        val session = props.session
        div {
            attrs.id = "banner"
            AtlassianNavigation {
                attrs.label = "Test"
                attrs.renderProductHome = {
                    CustomProductHome {
                        attrs.iconAlt = "http://localhost:8080/frontend/favicon.png"
                        attrs.iconUrl = "http://localhost:8080/frontend/favicon.png"
                        attrs.logoAlt = "http://localhost:8080/frontend/favicon.png"
                        attrs.logoUrl = "http://localhost:8080/frontend/favicon.png"
                        attrs.siteTitle = "Cockpit"
                    }
                }
                if (config != null && session != null) {
                    val icon = img {
                        attrs.src =
                            "https://w7.pngwing.com/pngs/7/618/png-transparent-man-illustration-avatar-icon-fashion-men-avatar-face-fashion-girl-heroes.png"
                        attrs.jsStyle {
                            borderRadius = "50%"
                            width = 32
                            height = 32
                        }
                    }
                    attrs.renderProfile = {
                        Popup {
                            attrs.isOpen = state.profilePopupIsOpen
                            attrs.onClose = {
                                setState { profilePopupIsOpen = false }
                            }
                            attrs.placement = "bottom-start"
                            attrs.trigger = { triggerProps ->
                                Profile {
                                    Object.keys(triggerProps).forEach { key ->
                                        val descriptor = Object.getOwnPropertyDescriptor<RProps>(triggerProps, key)
                                        Object.defineProperty(attrs, key, descriptor)
                                    }
                                    attrs.icon = icon
                                    attrs.onClick = {
                                        setState { profilePopupIsOpen = !profilePopupIsOpen }
                                    }
                                }
                            }
                            attrs.content = { _ ->
                                MenuGroup {
                                    LinkItem {
                                        +"Logout"
                                        attrs.onClick = {
                                            setState { profilePopupIsOpen = false }
                                            SessionHandler.logout()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    interface State : RState {
        var profilePopupIsOpen: Boolean
    }

}
