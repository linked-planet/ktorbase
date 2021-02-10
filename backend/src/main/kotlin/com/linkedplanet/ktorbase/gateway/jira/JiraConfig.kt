package com.linkedplanet.ktorbase.gateway.jira

import com.typesafe.config.ConfigFactory

object JiraConfig {

    private val config = ConfigFactory.load().getConfig("jira")
    val username: String = config.getString("username")
    val password: String = config.getString("password")
    val baseUrl: String = config.getString("baseUrl")
    val projectKey: String = config.getString("projectKey")
    val issueTypeName: String = config.getString("issueTypeName")

    object CustomFieldNameConfig {
        private val customFieldNameConfig = config.getConfig("customFieldName")
        val service: String = customFieldNameConfig.getString("service")
        val incidentId: String = customFieldNameConfig.getString("incidentId")
    }

    object Workflow {
        private val workflowConfig = config.getConfig("workflow")

        object StatusName {
            private val statusNameConfig = workflowConfig.getConfig("statusName")
            val closed: String = statusNameConfig.getString("closed")
        }

        object TransitionName {
            private val transitionNameConfig = workflowConfig.getConfig("transitionName")
            val close: String = transitionNameConfig.getString("close")
            val reopenClosed: String = transitionNameConfig.getString("reopenClosed")
        }
    }

}