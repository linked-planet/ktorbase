@file:JsModule("@atlaskit/atlassian-navigation")

package imports.atlaskit.navigation

import imports.atlaskit.button.ButtonProps
import org.w3c.dom.events.MouseEvent
import react.RClass
import react.RProps
import react.ReactElement

@JsName("AtlassianNavigation")
external val AtlassianNavigation: RClass<AtlassianNavigationProps>

external interface AtlassianNavigationProps : RProps {

    var label: String

    var primaryItems: Array<ReactElement>

    var renderProductHome: () -> ReactElement

    var renderProfile: () -> ReactElement

}

@JsName("CustomProductHome")
external val CustomProductHome: RClass<CustomProductHomeProps>

external interface CustomProductHomeProps : RProps {

    var iconAlt: String

    var iconUrl: String

    var logoAlt: String

    var logoUrl: String

    var siteTitle: String?

}

@JsName("PrimaryButton")
external val PrimaryButton: RClass<PrimaryButtonProps>

external interface PrimaryButtonProps : RProps {

    var isHighlighted: Boolean

    var testId: String

    var tooltip: ReactElement

    var onClick: (MouseEvent) -> Unit

}

@JsName("Profile")
external val Profile: RClass<ProfileProps>

external interface ProfileProps : ButtonProps {

    var icon: dynamic

}
