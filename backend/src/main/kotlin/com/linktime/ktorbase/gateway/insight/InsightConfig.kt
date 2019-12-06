package com.linktime.ktorbase.gateway.insight

import com.typesafe.config.ConfigFactory

object InsightConfig {

    private val config = ConfigFactory.load().getConfig("insight")
    val schemaId: Int = config.getInt("schemaId")

}