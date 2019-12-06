package atlaskit.icon

import react.RProps

interface IconProps : RProps {
    /**
     * For primary colour for icons
     */
    var primaryColor: String

    /**
     * For secondary colour for 2-color icons. Set to inherit to control this via "fill" in CSS
     */
    var secondaryColor: String

    /**
     * Control the size of the icon.
     * One of ['small', 'medium', 'large', 'xlarge']
     */
    var size: String
}