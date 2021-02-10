@file:JsModule("@atlaskit/checkbox")

package imports.atlaskit.checkbox

import org.w3c.dom.events.Event
import react.RClass
import react.RProps

@JsName("Checkbox")
external val Checkbox: RClass<CheckboxProps>

external interface CheckboxProps : RProps {

    /**
     * Sets whether the checkbox is checked or unchecked.
     */
    var isChecked: Boolean

    /**
     * Sets whether the checkbox is disabled.
     */
    var isDisabled: Boolean

    /**
     * Sets whether the checkbox should take up the full width of the parent.
     */
    var isFullWidth: Boolean

    /**
     * Marks the field as invalid. Changes style of unchecked component.
     */
    var isInvalid: Boolean

    /**
     * Marks the field as required & changes the label style.
     */
    var isRequired: Boolean

    /**
     * The label to be displayed to the right of the checkbox.
     * The label is part of the clickable element to select the checkbox.
     */
    var label: String

    /**
     * The value to be used in the checkbox input. This is the value that will be returned on form submission.
     */
    var value: String?

    var onChange: (Event) -> Unit

}
