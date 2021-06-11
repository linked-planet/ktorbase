package com.linkedplanet.ktorbase.config

import com.typesafe.config.ConfigFactory

object AppConfig {

    private val config = ConfigFactory.load().getConfig("app")
    val title: String = config.getString("title")
    val bannerBackgroundColor: String = config.getString("bannerBackgroundColor")
    val bannerMenuBackgroundColor: String = config.getString("bannerMenuBackgroundColor")
    val ssoSaml: Boolean = config.getBoolean("sso.saml")
    val buildVersion: String = config.getString("buildVersion")

}
