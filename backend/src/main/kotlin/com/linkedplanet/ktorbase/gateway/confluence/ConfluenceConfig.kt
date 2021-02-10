package com.linkedplanet.ktorbase.gateway.confluence

import com.typesafe.config.ConfigFactory

object ConfluenceConfig {

    private val config = ConfigFactory.load().getConfig("confluence")
    val username: String = config.getString("username")
    val password: String = config.getString("password")
    val baseUrl: String = config.getString("baseUrl")

    object KbConfig {
        private val kbConfig = config.getConfig("kb")
        val spaceKey: String = kbConfig.getString("spaceKey")
        val homePageId: Int = kbConfig.getInt("homePageId")
        val searchExcludeLabels: Set<String> = kbConfig.getStringList("searchExcludeLabels").toSet()
        val excerptWordLimit: Int = kbConfig.getInt("excerptWordLimit")

        object PageIdConfig {
            private val idConfig = kbConfig.getConfig("page.id")
            val welcome: Int = idConfig.getInt("welcome")
        }
    }


}