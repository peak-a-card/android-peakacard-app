package com.peakacard.session.data.datasource.remote.model

import com.peakacard.user.data.datasource.remote.model.UserDataModel

data class SessionRequest(val user: UserDataModel, val code: String)
