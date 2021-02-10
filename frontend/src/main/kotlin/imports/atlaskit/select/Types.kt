package imports.atlaskit.select

import kotlin.js.Json

data class SelectOption(val label: String, val value: String)

data class SelectStyles(
    val clearIndicator: (Json, dynamic) -> Json = { provided, _ ->
        noOp("clearIndicator", provided)
    },
    val container: (Json, dynamic) -> Json = { provided, _ ->
        noOp("container", provided)
    },
    val control: (Json, dynamic) -> Json = { provided, _ ->
        noOp("control", provided)
    },
    val dropdownIndicator: (Json, dynamic) -> Json = { provided, _ ->
        noOp("dropdownIndicator", provided)
    },
    val group: (Json, dynamic) -> Json = { provided, _ ->
        noOp("group", provided)
    },
    val groupHeading: (Json, dynamic) -> Json = { provided, _ ->
        noOp("groupHeading", provided)
    },
    val indicatorsContainer: (Json, dynamic) -> Json = { provided, _ ->
        noOp("indicatorsContainer", provided)
    },
    val indicatorSeparator: (Json, dynamic) -> Json = { provided, _ ->
        noOp("indicatorSeparator", provided)
    },
    val input: (Json, dynamic) -> Json = { provided, _ ->
        noOp("input", provided)
    },
    val loadingIndicator: (Json, dynamic) -> Json = { provided, _ ->
        noOp("loadingIndicator", provided)
    },
    val loadingMessage: (Json, dynamic) -> Json = { provided, _ ->
        noOp("loadingMessage", provided)
    },
    val menu: (Json, dynamic) -> Json = { provided, _ ->
        noOp("menu", provided)
    },
    val menuList: (Json, dynamic) -> Json = { provided, _ ->
        noOp("menuList", provided)
    },
    val menuPortal: (Json, dynamic) -> Json = { provided, _ ->
        noOp("menuPortal", provided)
    },
    val multiValue: (Json, dynamic) -> Json = { provided, _ ->
        noOp("multiValue", provided)
    },
    val multiValueLabel: (Json, dynamic) -> Json = { provided, _ ->
        noOp("multiValueLabel", provided)
    },
    val multiValueRemove: (Json, dynamic) -> Json = { provided, _ ->
        noOp("multiValueRemove", provided)
    },
    val noOptionsMessage: (Json, dynamic) -> Json = { provided, _ ->
        noOp("noOptionsMessage", provided)
    },
    val option: (Json, dynamic) -> Json = { provided, _ ->
        noOp("option", provided)
    },
    val placeholder: (Json, dynamic) -> Json = { provided, _ ->
        noOp("placeholder", provided)
    },
    val singleValue: (Json, dynamic) -> Json = { provided, _ ->
        noOp("singleValue", provided)
    },
    val valueContainer: (Json, dynamic) -> Json = { provided, _ ->
        noOp("valueContainer", provided)
    }
) {
    companion object {
        @Suppress("unused") // available for debugging purposes
        fun log(label: String, provided: Json): Json {
            console.log(label)
            console.log(provided)
            return provided
        }

        fun noOp(@Suppress("UNUSED_PARAMETER") label: String, provided: Json): Json = provided
    }
}
