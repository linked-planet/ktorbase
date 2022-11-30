package com.linkedplanet.ktorbase.component.pages.mainPage

import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Session
import react.Props
import react.RBuilder
import react.dom.div
import react.dom.p
import react.fc

external interface MainPageProps : Props {
    var session: Session
    var config: Config
}

private val MainPage = fc<MainPageProps> { props ->
    val greetUsername = props.session.username
    div {
        p { +"Hello $greetUsername" }
        p { +"Config BannerBackgroundColor: ${props.config.bannerBackgroundColor}" }
        p { +"Config BannerMenuBackgroundColor: ${props.config.bannerMenuBackgroundColor}" }
    }
}

fun RBuilder.MainPage(handler: MainPageProps.() -> Unit) =
    child(MainPage) { attrs { handler() } }
