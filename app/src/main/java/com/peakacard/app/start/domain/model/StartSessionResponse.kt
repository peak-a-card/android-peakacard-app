package com.peakacard.app.start.domain.model

sealed class StartSessionResponse {
    object Success : StartSessionResponse()

    sealed class Error : StartSessionResponse() {
        object NoSessionFound : Error()
        object Unspecified : Error()
    }
}