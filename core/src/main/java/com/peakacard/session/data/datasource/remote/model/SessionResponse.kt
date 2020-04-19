package com.peakacard.session.data.datasource.remote.model

sealed class SessionResponse {
    object Success : SessionResponse()

    sealed class Error : SessionResponse() {
        object NoSessionFound : Error()
        object RemoteException : Error()
    }
}
