@file:JsModule("@atlaskit/avatar")

package imports.atlaskit.avatar

import org.w3c.dom.events.MouseEvent
import react.RClass
import react.RProps

@JsName("default")
external val Avatar: RClass<AvatarProps>

external interface AvatarProps : RProps {
    var src: String
    var name: String
    var size: String
    var presence: String
}

@JsName("AvatarItem")
external val AvatarItem: RClass<AvatarItemProps>

external interface AvatarItemProps : RProps {
    var avatar: dynamic
    var key: String
    var primaryText: String
    var secondaryText: String
    var onClick: (MouseEvent) -> Unit
}
