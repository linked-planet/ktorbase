package com.linkedplanet.ktorbase.config

import com.typesafe.config.*
import java.time.Duration

object SessionConfig {

    private val config: Config = ConfigFactory.parseResourcesAnySyntax("session").resolve().getConfig("session")
    val expiration: Duration = config.getDuration("expiration")

}
