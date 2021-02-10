@file:JsModule("@atlaskit/inline-edit")

package imports.atlaskit.inlineedit

import react.RClass
import react.RProps
import react.ReactElement

@JsName("default")
external val InlineEdit: RClass<InlineEditProps>

external interface InlineEditProps : RProps {

    var label: String

    var readView: () -> ReactElement

    var editView: (RProps) -> ReactElement

    var onConfirm: (dynamic) -> Unit

    var defaultValue: dynamic

    var startWithEditViewOpen: Boolean

    var hideActionButtons: Boolean
}
