@file:JsModule("@atlaskit/menu")

package imports.atlaskit.menu

import org.w3c.dom.events.Event
import react.RClass
import react.RProps
import react.ReactElement

@JsName("MenuGroup")
external val MenuGroup: RClass<MenuGroupProps>

external interface MenuGroupProps : RProps {

    var children: ReactElement

}

@JsName("LinkItem")
external val LinkItem: RClass<LinkItemProps>

external interface LinkItemProps : RProps {

    var onClick: (Event) -> Unit

}
