package imports.atlaskit.inlineedit

import imports.atlaskit.textfield.Textfield
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.div
import react.redux.rConnect
import redux.WrapperAction

interface InlineTextfieldProps : RProps {
    var label: String
    var value: String
    var onSave: (dynamic) -> Unit
    var disabled: Boolean
}

interface InlineTextfieldState : RState {
    var value: String?
}

val inlineTextfieldComponent: RClass<InlineTextfieldProps> =
    rConnect<InlineTextfield, WrapperAction>()(InlineTextfield::class.js.unsafeCast<RClass<RProps>>())

class InlineTextfield(props: InlineTextfieldProps) : RComponent<InlineTextfieldProps, InlineTextfieldState>(props) {

    override fun componentWillReceiveProps(nextProps: InlineTextfieldProps) {
        setState {
            value = nextProps.value
        }
    }

    override fun RBuilder.render() {
        div {
            InlineEdit {
                attrs.defaultValue = props.value
                attrs.label = props.label
                attrs.hideActionButtons = props.disabled
                attrs.editView = { fieldprops ->
                    if (props.disabled) {
                        div(classes = "inline-edit-read-view") {
                            +props.value
                        }
                    } else {
                        Textfield {
                            attrs.autoFocus = true
                            attrs.defaultValue = props.value
                            attrs.onChange = {
                                val target = it.target as HTMLInputElement
                                setState {
                                    value = target.value
                                }
                            }
                        }
                    }
                }
                attrs.readView = {
                    div(classes = "inline-edit-read-view") {
                        +props.value
                    }
                }
                attrs.onConfirm = { value ->
                    if (state.value != props.value) {
                        console.log("update: '$value'")
                        props.onSave(state.value)
                    }
                }
            }
        }
    }
}
