package imports.atlaskit.inlineedit

import imports.atlaskit.select.Select
import imports.atlaskit.select.SelectOption
import react.*
import react.dom.div
import react.redux.rConnect
import redux.WrapperAction


interface InlineSelectfieldState : RState {
    var value: String?
}

interface InlineSelectfieldProps : RProps {
    var label: String
    var options: List<String>
    var value: String
    var onSave: (dynamic) -> Unit
}
val inlineSelectfieldComponent: RClass<InlineSelectfieldProps> =
    rConnect<InlineSelectfield, WrapperAction>()(InlineSelectfield::class.js.unsafeCast<RClass<RProps>>())

class InlineSelectfield(props: InlineSelectfieldProps) : RComponent<InlineSelectfieldProps, InlineSelectfieldState>(props) {

    override fun componentWillReceiveProps(nextProps: InlineSelectfieldProps) {
        setState {
            value = nextProps.value
        }
    }

    override fun RBuilder.render() {
        div {
            InlineEdit {
                attrs.defaultValue = props.value
                attrs.label = props.label
                attrs.readView = {
                    div(classes = "inline-edit-read-view") {
                        +props.value
                    }
                }
                attrs.editView = { fieldprops ->
                    Select {
                        val options = props.options.map { SelectOption(it, it) }.toTypedArray()
                        attrs.autoFocus = true
                        attrs.options = options
                        attrs.value = options.firstOrNull { it.value == state.value }
                        attrs.onChange = {
                            val v = it.value
                            console.log("change state to $v")
                            console.log(props.options.map { SelectOption(it, it) }.toTypedArray())
                            setState {
                                value = v
                            }
                        }
                    }
                }
                attrs.onConfirm = { value ->
                    props.onSave(state.value)
                }
            }
        }
    }
}
