package com.linktime.ktorbase.component.main

import com.linktime.ktorbase.model.Config
import kotlinx.html.id
import react.*
import react.dom.div
import react.dom.span
import kotlin.browser.document

class MainComponent : RComponent<MainComponent.Props, MainComponent.State>() {

    override fun RBuilder.render() {
        div {
            attrs.id = "main-content"
            span {
                +"Hello! Here we are!"
            }
        }
    }

    interface Props : RProps {
        var config: Config
    }

    interface State : RState {
    }

}

fun RBuilder.mainComponent(config: Config) =
    child(MainComponent::class) {
        attrs.config = config
    }