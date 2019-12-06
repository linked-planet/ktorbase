package atlaskit.modal

import react.RProps

data class ModalAction(val text: String, val onClick: () -> Unit)

data class ModalComponents(
    var Header: dynamic = undefined,
    var Footer: dynamic = undefined,
    var Body: dynamic = undefined,
    var Container: dynamic = undefined
)

interface FooterProps : RProps {
    var onClose: () -> Unit
}