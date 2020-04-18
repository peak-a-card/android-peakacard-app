package com.peakacard.host.session.data.datasource.remote.model

sealed class SessionResponse {
    object Success : SessionResponse()
    object RemoteError : SessionResponse()
}
