package com.peakacard.app.session.domain.model

sealed class LeaveSessionResponse {
    object Success : LeaveSessionResponse()

    sealed class Error : LeaveSessionResponse() {
        object NoSessionFound : Error()
        object NoUserFound : Error()
        object Unspecified : Error()
    }
}
