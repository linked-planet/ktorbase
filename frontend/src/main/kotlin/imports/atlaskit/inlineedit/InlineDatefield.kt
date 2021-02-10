package imports.atlaskit.inlineedit

import imports.atlaskit.textfield.Textfield
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.div
import react.redux.rConnect
import redux.WrapperAction

interface InlineDatefieldProps : RProps {
    var label: String
    var value: String
    var onSave: (dynamic) -> Unit
    var disabled: Boolean
}

interface InlineDatefieldState : RState {
    var value: String?
}

val inlineDatefieldComponent: RClass<InlineDatefieldProps> =
    rConnect<InlineDatefield, WrapperAction>()(InlineDatefield::class.js.unsafeCast<RClass<RProps>>())

class InlineDatefield(props: InlineDatefieldProps) : RComponent<InlineDatefieldProps, InlineDatefieldState>(props) {

    override fun componentWillReceiveProps(nextProps: InlineDatefieldProps) {
        setState {
            value = nextProps.value
        }
    }

    override fun componentWillUpdate(nextProps: InlineDatefieldProps, nextState: InlineDatefieldState) {
        if(state.value == null) {
            nextState.value = nextProps.value
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
                            attrs.type = "date"
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
                        props.onSave(state.value)
                    }
                }
            }
        }
    }
}
