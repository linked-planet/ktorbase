@file:JsModule("@atlaskit/menu")

package imports.atlaskit.menu

import org.w3c.dom.events.Event
import react.ComponentClass
import react.Props
import react.ReactNode

@JsName("MenuGroup")
external val MenuGroup: ComponentClass<MenuGroupProps>

external interface MenuGroupProps : Props {

    var children: ReactNode

}

@JsName("LinkItem")
external val LinkItem: ComponentClass<LinkItemProps>

external interface LinkItemProps : Props {

    var onClick: (Event) -> Unit

}
