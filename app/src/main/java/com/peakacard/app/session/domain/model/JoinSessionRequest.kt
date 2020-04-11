package com.peakacard.app.session.domain.model

data class JoinSessionRequest(val user: User, val sessionCode: String)
