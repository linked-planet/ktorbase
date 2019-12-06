@file:JsModule("@atlaskit/flag")

package atlaskit.flag

import react.RClass
import react.RProps
import react.ReactElement

@JsName("default")
external val Flag: RClass<FlagProps>

external interface FlagProps : RProps {
    /**
     * Array of clickable actions to be shown at the bottom of the flag.
     * For flags where appearance is 'normal', actions will be shown as links.
     * For all other appearance values, actions will shown as buttons.
     * If href is passed the action will be shown as a link with the passed href prop.
     */
    var actions: Array<FlagAction>

    /**
     * Makes the flag appearance bold. Setting this to anything other than 'normal' hides the dismiss button.
     * One of ["error", "info", "normal", "success", "warning"]
     */
    var appearance: String

    /**
     * The icon displayed in the top-left of the flag. Should be an instance of @atlaskit/icon.
     * Your icon will receive the appropriate default color, which you can override by wrapping the icon in a
     * containing element with CSS color set to your preferred icon color.
     */
    var icon: ReactElement?

    /**
     * The secondary content shown below the flag title
     */
    var description: String?

    /**
     * The bold text shown at the top of the flag.
     */
    var title: String

    /**
     * A unique identifier used for rendering and onDismissed callbacks.
     */
    var id: String
}

@JsName("AutoDismissFlag")
external val AutoDismissFlag: RClass<FlagProps>

@JsName("FlagGroup")
external val FlagGroup: RClass<FlagGroupProps>

external interface FlagGroupProps : RProps {
    var onDismissed: (String) -> Unit
}