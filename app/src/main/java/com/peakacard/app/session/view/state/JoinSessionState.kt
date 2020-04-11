package com.peakacard.app.session.view.state

sealed class JoinSessionState {
    object JoiningSession : JoinSessionState()
    object Joined : JoinSessionState()
    sealed class Error : JoinSessionState() {
        object UserSignInError : Error()
        object CodeRequiredError : Error()
        object NoSessionFound : Error()
        object Unspecified : Error()
    }
}
