@file:JsModule("@atlaskit/page-layout")
package imports.atlaskit.pagelayout

import react.RClass
import react.RProps
import react.ReactElement

@JsName("PageLayout")
external val PageLayout: RClass<PageLayoutProps>

external interface PageLayoutProps : RProps {

    var id: String

    var children: ReactElement
}

@JsName("Banner")
external val Banner: RClass<BannerProps>

external interface BannerProps : RProps {

    var id: String

    var children: ReactElement

    var height: Int
}

@JsName("Main")
external val Main: RClass<MainProps>

external interface MainProps : RProps {

    var id: String

    var children: ReactElement

    var width: Int
}

@JsName("Content")
external val Content: RClass<ContentProps>

external interface ContentProps : RProps {

    var id: String

    var children: ReactElement

    var width: Int
}

@JsName("RightSidebar")
external val RightSidebar: RClass<RightSidebarProps>

external interface RightSidebarProps : RProps {

    var id: String

    var children: ReactElement

    var width: Int

    var isFixed: Boolean


}

@JsName("LeftSidebar")
external val LeftSidebar: RClass<LeftSidebarProps>

external interface LeftSidebarProps : RProps {

    var id: String

    var children: ReactElement

    var width: Int

    var isFixed: Boolean

    var onCollapse: () -> Unit

    var onExpand: () -> Unit

    var onResizeStart: () -> Unit

    var onResizeEnd: () -> Unit

    var onFlyoutExpand: () -> Unit

}

@JsName("TopNavigation")
external val TopNavigation: RClass<TopNavigationProps>

external interface TopNavigationProps : RProps {

    var id: String

    var children: ReactElement

    var height: Int
}
