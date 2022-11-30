package com.linkedplanet.ktorbase

import com.linkedplanet.ktorbase.component.MainComponent
import kotlinx.browser.document
import org.w3c.dom.Element
import react.Props
import react.createElement
import react.dom.client.createRoot

@OptIn(ExperimentalJsExport::class)
@JsExport
fun main() {
    // css
    kotlinext.js.require("app.scss")
    kotlinext.js.require("banner.scss")
    kotlinext.js.require("login.scss")

    // favicon
    kotlinext.js.require("favicon.png")

    // main entry point
    val container = document.getElementById("content") as Element
    val root = createRoot(container)
    root.render(createElement<Props> {
        MainComponent {}
    })
}
