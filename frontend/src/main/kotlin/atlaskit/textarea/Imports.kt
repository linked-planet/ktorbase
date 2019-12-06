@file:JsModule("@atlaskit/textarea")

package atlaskit.textarea

import org.w3c.dom.events.Event
import react.RClass
import react.RProps
import kotlin.js.Json

@JsName("default")
external val Textarea: RClass<TextareaProps>

external interface TextareaProps : RProps {

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
     * Sets a default value as input value
     */
    var defaultValue: String?

    /**
     * Sets the field as uneditable, with a changed hover state.
     */
    var isDisabled: Boolean

    /**
     * Sets styling to indicate that the input is focused.
     */
    var isFocused: Boolean

    /**
     * Sets styling to indicate that the input is invalid
     */
    var isInvalid: Boolean

    /**
     * Sets content text value to monospace
     */
    var isMonospaced: Boolean

    /**
     * If true, prevents the value of the input from being edited.
     */
    var isReadOnly: Boolean

    /**
     * Set required for form that the field is part of.
     */
    var isRequired: Boolean

    /**
     * Sets maximum width of input
     * One of <`xsmall`, `small`, `medium`, `large`, `xlarge`, custom width (e.g. `546`), `default` (100%)>
     */
    var width: String

    /**
     * The value of the input.
     */
    var value: String?

    var placeholder: String

    var onChange: (Event) -> Unit

    /**
     * The minimum number of rows of text to display
     */
    var minimumRows: Int

    /**
     * The maxheight of the textarea
     */
    var maxHeight: String

    /**
     * Enables the resizing of the textarea:
     * - auto: both directions.
     * - horizontal: only along the x axis.
     * - vertical: only along the y axis.
     * - smart (default): vertically grows and shrinks the textarea automatically to wrap your input text.
     * - none: explicitly disallow resizing on the textarea.
     */
    var resize: String

    var theme: ((dynamic) -> dynamic, dynamic) -> dynamic

}
