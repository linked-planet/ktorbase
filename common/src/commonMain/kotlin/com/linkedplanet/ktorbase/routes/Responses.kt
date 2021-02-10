package com.linkedplanet.ktorbase.routes

import com.linkedplanet.ktorbase.model.Config
import com.linkedplanet.ktorbase.model.Session


data class SessionResponse(val session: Session, val config: Config)