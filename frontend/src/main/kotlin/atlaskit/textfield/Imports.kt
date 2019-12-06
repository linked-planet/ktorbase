@file:JsModule("@atlaskit/textfield")

package atlaskit.textfield

import org.w3c.dom.events.Event
import org.w3c.dom.events.KeyboardEvent
import react.RClass
import react.RProps
import kotlin.js.Json

@JsName("default")
external val Textfield: RClass<TextfieldProps>

external interface TextfieldProps : RProps {

    /**
     * Controls the appearance of the field. subtle shows styling on hover. none hides all field styling.
     * One of <`standard`, `none`, `subtle`>
     */
    var appearance: String

    /**
     * Applies compact styling, making the field smaller
     */
    var isCompact: Boolean

    /**
     * Sets the field as uneditable, with a changed hover state.
     */
    var isDisabled: Boolean

    /**
     * If true, prevents the value of the input from being edited.
     */
    var isReadOnly: Boolean

    /**
     * Set required for form that the field is part of.
     */
    var isRequired: Boolean

    /**
     * Sets styling to indicate that the input is invalid
     */
    var isInvalid: Boolean

    /**
     * Sets styling to indicate that the input is focused
     */
    var isFocused: Boolean

    /**
     * Sets a default value as input value
     */
    var defaultValue: String?

    /**
     * Sets content text value to monospace
     */
    var isMonospaced: Boolean

    /**
     * The value of the input.
     */
    var value: String?

    var placeholder: String

    var onChange: (Event) -> Unit

    var onKeyDown: (KeyboardEvent) -> Unit

    var autoFocus: Boolean

    var maxLength: Int

    var id: String

    var type: String

    var list: String

    /**
     * on | off
     */
    var autoComplete: String

    var theme: ((dynamic) -> dynamic, dynamic) -> dynamic

}
