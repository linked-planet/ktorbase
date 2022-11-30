package com.linkedplanet.ktorbase.request

import com.linkedplanet.ktorbase.routes.LoginBody
import com.linkedplanet.ktorbase.routes.SessionResponse
import com.linkedplanet.uikit.util.BadStatusCodeException
import com.linkedplanet.uikit.util.RequestUtil.requestAndHandleSuccess
import com.linkedplanet.uikit.util.RequestUtil.requestAndParseResult
import kotlin.js.json

object SessionRequest {

    suspend fun currentUser(): SessionResponse =
        requestAndParseResult(
            url = "/session",
            method = "GET",
            headers = json(
                "Accept" to "application/json"
            ),
            parse = { JSON.parse(JSON.stringify(it)) }
        )

    suspend fun login(username: String, password: String): SessionResponse? =
        try {
            requestAndParseResult(
                url = "/session",
                method = "POST",
                headers = json(
                    "Content-Type" to "application/json"
                ),
                body = JSON.stringify(LoginBody(username, password)),
                parse = { JSON.parse(JSON.stringify(it)) }
            )
        } catch (e: BadStatusCodeException) {
            if (e.statusCode == 401) null
            else throw e
        }

    suspend fun logout() =
        requestAndHandleSuccess(
            url = "/session",
            method = "DELETE",
            headers = json(),
            handler = { }
        )

}
