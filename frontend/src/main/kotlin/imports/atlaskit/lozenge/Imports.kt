@file:JsModule("@atlaskit/lozenge")

package imports.atlaskit.lozenge

import react.RClass
import react.RProps

@JsName("default")
external val Lozenge: RClass<LozengeProps>

external interface LozengeProps : RProps {

    /**
     * One of "default", "inprogress", "moved", "new", "removed", "success"
     */
    var appearance: String

    /**
     * Determines whether to apply the bold style or not.
     */
    var isBold: Boolean

    /**
     * max-width of lozenge container. Default to 200px.
     */
    var maxWidth: String

}
