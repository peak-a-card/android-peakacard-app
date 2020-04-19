package com.peakacard.session.domain.model

import com.peakacard.user.domain.model.User

data class UserSession(val user: User, val sessionCode: String)
