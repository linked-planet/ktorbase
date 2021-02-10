package com.linkedplanet.ktorbase.component.pages.mainPage

import com.linkedplanet.ktorbase.AppState
import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Session
import react.*
import react.dom.div
import react.dom.span
import react.redux.rConnect
import redux.RAction
import redux.WrapperAction

interface MainPageStateProps : RProps {
    var session: Session?
    var config: Config?
    var chaosMode: Boolean
}

interface MainPageDispatchProps : RProps
interface MainPageProps : MainPageStateProps, MainPageDispatchProps

val mainPage: RClass<MainPageProps> =
    rConnect<AppState, RAction, WrapperAction, RProps, MainPageStateProps, MainPageDispatchProps, MainPageProps>(
        { state, _ ->
            session = state.session
            config = state.config
            chaosMode = state.chaosMode
        },
        { _, _ -> }
    )(MainPage::class.js.unsafeCast<RClass<MainPageProps>>())

class MainPage(props: MainPageProps) : RComponent<MainPageProps, MainPage.State>(props) {


    override fun RBuilder.render() {
        div {
            span { +"Hello world" }
            span { +"Config BannerBackgroundColor: ${props.config?.bannerBackgroundColor?:"Not set"}" }
            span { +"Config BannerMenuBackgroundColor: ${props.config?.bannerMenuBackgroundColor?:"Not set"}" }
        }
    }

    interface State : RState {

    }

}