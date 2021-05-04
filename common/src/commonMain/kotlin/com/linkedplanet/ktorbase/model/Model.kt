package com.linkedplanet.ktorbase.model

// --------------------------------------------------------
// SESSION
// --------------------------------------------------------

data class Session(
    val username: String,
    val expireDate: String
)

// --------------------------------------------------------
// NOTIFICATION
// --------------------------------------------------------

enum class NotificationType {
    WARNING, INFO, ERROR, SUCCESS
}

data class Notification(
    val type: NotificationType,
    val title: String,
    val description: String
)

// --------------------------------------------------------
// CONFIG
// --------------------------------------------------------

data class Config(
    val bannerBackgroundColor: String,
    val bannerMenuBackgroundColor: String,
    val buildVersion: String
)
