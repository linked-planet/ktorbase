@file:JsModule("@atlaskit/button")

package imports.atlaskit.button

import org.w3c.dom.events.Event
import react.RClass
import react.RProps

@JsName("default")
external val Button: RClass<ButtonProps>

external interface ButtonProps : RProps {

    /**
     * Sets whether the checkbox is checked or unchecked.
     */
    var isActive: Boolean

    /**
     * Sets whether the checkbox is checked or unchecked.
     */
    var isHover: Boolean

    /**
     * Sets whether the checkbox is checked or unchecked.
     */
    var isFocus: Boolean

    /**
     * Sets whether the checkbox is checked or unchecked.
     */
    var isSelected: Boolean

    /**
     * Sets whether the checkbox is disabled.
     */
    var isDisabled: Boolean

    /**
     * Sets whether the checkbox is disabled.
     */
    var appearance: String

    /**
     * The value to be used in the checkbox input. This is the value that will be returned on form submission.
     */
    var value: String?

    var iconBefore: dynamic

    var className: String

    var onClick: (Event) -> Unit

}
