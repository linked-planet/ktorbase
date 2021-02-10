@file:JsModule("@atlaskit/dynamic-table")

package imports.atlaskit.table

import react.RClass
import react.RProps

@JsName("default")
external val DynamicTable: RClass<DynamicTableProps>

external interface DynamicTableProps : RProps {

    /**
     * Sets the caption (Node or String)
     */
    var caption: dynamic

    /**
     * Sets the head
     */
    var head: DynamicTableHead

    var rows: Array<DynamicTableRow>

    var defaultSortKey: String

    var defaultSortOrder: String

    var highlightedRowIndex: Int

    var testId: String

    var onSort: () -> Unit
    var onSetPage: () -> Unit

}
