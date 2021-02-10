@file:JsModule("@atlaskit/dropdown-menu")

package imports.atlaskit.dropdownmenu

import imports.atlaskit.button.ButtonProps
import org.w3c.dom.events.Event
import react.RClass
import react.RProps

@JsName("default")
/**
 * This is the root component that you will use for every dropdown menu.
 *
 * It comes with all state management out of the box.
 *
 * Need to programmatically control the open state? You'll want to use DropdownMenuStateless instead.
 */
external val DropdownMenu: RClass<DropdownMenuProps>

external interface DropdownMenuProps : RProps {

    /**
     * Called when the menu should be open/closed. Received an object with isOpen state.
     */
    var onOpenChange: () -> Unit

    /**
     * Controls the appearance of the menu.
     *
     * Default menu has scroll after its height exceeds the pre-defined amount.
     *
     * Tall menu has no scroll until the height exceeds the height of the viewport.
     *
     * One of <"default", "tall">
     */
    var appearance: String

    /**
     * Value passed to the Layer component to determine when to reposition the droplist
     *
     * One of <"viewport", "window", "scrollParent">
     */
    var boundariesElement: String

    /**
     * If true, a Spinner is rendered instead of the items
     */
    var isLoading: Boolean

    /**
     * Controls the open state of the dropdown.
     */
    var isOpen: Boolean

    /**
     * Position of the menu. See the documentation of @atlaskit/layer for more details.
     */
    var position: String

    /**
     * Determines if the dropdown menu should be positioned fixed.
     *
     * Useful for breaking out of overflow scroll/hidden containers, however, extra layout
     * management will be required to control scroll behaviour when this property is
     * enabled as the menu will not update position with the target on scroll.
     */
    var isMenuFixed: Boolean

    /**
     * Option to fit dropdown menu width to its parent width
     */
    var shouldFitContainer: Boolean

    /**
     * Allows the dropdown menu to be placed on the opposite side of its trigger if it does not fit in the viewport.
     */
    var shouldFlip: Boolean

    /**
     * Content which will trigger the dropdown menu to open and close.
     *
     * Use with triggerType to easily get a button trigger.
     *
     * One of <react.ReactNode, string>
     */
    var trigger: dynamic

    /**
     * Props to pass through to the trigger button.
     *
     * See @atlaskit/button for allowed props.
     */
    var triggerButtonProps: ButtonProps

    /**
     * Controls the interface of trigger to be used for the dropdown menu.
     *
     * The default trigger allows you to supply your own trigger component.
     *
     * Setting this prop to button will render a Button component with an
     * 'expand' icon, and the trigger prop contents inside the button.
     *
     * One of <"default", "button">
     */
    var triggerType: String

    /**
     * Callback to know when the menu is correctly positioned after it is opened
     */
    var onPositioned: () -> Unit

    /**
     * A testId prop is provided for specified elements, which is a unique string
     * that appears as a data attribute data -testid in the rendered code, serving
     * as a hook for automated tests.
     *
     * As dropdown-menu is composed of different components, we passed down the
     * testId to the sub component you want to test:
     *
     * - testId--trigger to get the menu trigger.
     * - testId--content to get the dropdown content trigger.
     */
    var testId: String

}

/**
 * All items need to be wrapped in a group, there are three available for you to use depending what children it has.
 */
@JsName("DropdownItemGroupCheckbox")
external val DropdownItemGroupCheckbox: RClass<DropdownItemGroupCheckboxProps>

external interface DropdownItemGroupCheckboxProps : RProps {

    /**
     * Unique id used to enable selections.
     *
     * When using multiple groups make sure they each have a unique id.
     */
    var id: String

    /**
     * Optional heading text to be shown above the items.
     */
    var title: String

    /**
     * Content to be shown to the right of the title heading. Not shown if no title is set.
     *
     * One of <react.ReactNode, string>
     */
    var elemAfter: dynamic

}

/**
 * When wanting to present a user with groups that have a multiple selections.
 *
 * Every item should be inside a dropdown item group checkbox.
 */
@JsName("DropdownItemCheckbox")
external val DropdownItemCheckbox: RClass<DropdownItemCheckboxProps>

external interface DropdownItemCheckboxProps : RProps {

    /**
     * Unique identifier for the item, so that selection state can be tracked when the dropdown is opened/closed.
     */
    var id: String

    /**
     * Set at mount to make the item appear checked.
     *
     * The user may interact with the item after mount.
     *
     * See isSelected if you want to control the item state manually.
     */
    var defaultSelected: Boolean

    /**
     * Causes the item to appear visually checked.
     *
     * Can be set at mount time, and updated after mount.
     *
     * Changing the value will not cause onClick to be called.
     */
    var isSelected: Boolean

    /**
     * Standard optional onClick handler
     */
    var onClick: (Event) -> Unit

}
