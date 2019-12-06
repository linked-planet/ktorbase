package com.linktime.ktorbase.routes

import com.linktime.ktorbase.model.Config
import com.linktime.ktorbase.model.Session

data class SessionResponse(val session: Session, val config: Config)