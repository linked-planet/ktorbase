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
    val bannerMenuBackgroundColor: String
)

// --------------------------------------------------------
// Confluence
// --------------------------------------------------------

data class Language(
    val code: String,
    val displayName: String
)

data class KbPage(
    val id: Int,
    val title: String
)

data class KbSearchResult(
    val page: KbPage,
    val titleHighlight: String,
    val excerptHighlight: String
)

// --------------------------------------------------------
// Insight
// --------------------------------------------------------

interface InsightObject {
    val id: Int
    val name: String
}

data class Service(
    override val id: Int,
    override val name: String,
    val status: String?
) : InsightObject


// --------------------------------------------------------
// JIRA
// --------------------------------------------------------

data class Ticket(
    val key: String,
    val closed: Boolean,
    val data: TicketData
)

data class TicketData(
    val serviceId: Int?,
    val summary: String?,
    val description: String?,
    val incidentId: String
)