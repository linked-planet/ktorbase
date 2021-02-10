@file:JsModule("@atlaskit/select")

package imports.atlaskit.select

import react.RClass
import react.RProps

@JsName("default")
external val Select: RClass<SelectProps>

external interface SelectProps : RProps {

    var inputId: String

    var options: Array<SelectOption>

    var value: SelectOption?

    var placeholder: String

    var isCompact: Boolean

    var className: String

    var classNamePrefix: String

    var onChange: (SelectOption) -> Unit

    var autoFocus: Boolean

    var isDisabled: Boolean

    var styles: SelectStyles

}
