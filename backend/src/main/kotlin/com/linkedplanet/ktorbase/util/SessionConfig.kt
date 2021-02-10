package com.linkedplanet.ktorbase.util

import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import java.time.Duration

class SessionConfig(
    config: Config = ConfigFactory
        .parseResourcesAnySyntax("session")
        .resolve()
        .getConfig("session")
) {
    val expiration: Duration = config.getDuration("expiration")
}