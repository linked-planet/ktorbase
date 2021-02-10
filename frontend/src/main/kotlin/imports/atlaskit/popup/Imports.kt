@file:JsModule("@atlaskit/popup")

package imports.atlaskit.popup

import react.RClass
import react.RProps
import react.ReactElement

@JsName("Popup")
external val Popup: RClass<PopupProps>

external interface PopupProps : RProps {

    var isOpen: Boolean

    var trigger: (RProps) -> ReactElement

    var content: (RProps) -> ReactElement

    var boundariesElement: String

    var id: String

    var offset: String

    var placement: String

    var shouldFlip: Boolean

    var onClose: () -> Unit

    var popupComponent: () -> ReactElement

    var zIndex: Int

    var autoFocus: Boolean

}
