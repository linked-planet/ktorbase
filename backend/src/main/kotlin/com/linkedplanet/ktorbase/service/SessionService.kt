package com.linkedplanet.ktorbase.service

import com.linkedplanet.ktorbase.model.Session
import com.linkedplanet.ktorbase.util.SessionConfig
import org.joda.time.DateTime

object SessionService {

    fun createSession(username: String): Session =
        Session(username, getExpirationDateString())

    fun updateSession(session: Session): Session =
        Session(session.username, getExpirationDateString())

    fun validateSessionExpiration(session: Session?): Session? =
        session?.takeIf { DateTime.parse(session.expireDate).isAfterNow }

    private fun getExpirationDateString(): String =
        DateTime.now().plus(SessionConfig().expiration.toMillis()).toString()

}