package com.peakacard.app.start.view.state

sealed class StartSessionState {
    object StartingSession : StartSessionState()
    object Started : StartSessionState()
    sealed class Error : StartSessionState() {
        object NameRequiredError : Error()
        object CodeRequiredError : Error()
    }
}