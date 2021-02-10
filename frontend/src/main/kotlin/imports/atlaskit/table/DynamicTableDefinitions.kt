package imports.atlaskit.table

import org.w3c.dom.events.MouseEvent
import react.ReactElement

data class DynamicTableHead(
    val cells: Array<HeaderCell>
)

data class HeaderCell(
    val key: String,
    val isSortable: Boolean,
    val content: String
)

data class RowCell(
    val content: String
)

data class RowCellNode(
    val content: ReactElement
)

data class DynamicTableRow(
    val key: String,
    val cells: Array<dynamic>,
    val onClick: (MouseEvent) -> Unit
)

