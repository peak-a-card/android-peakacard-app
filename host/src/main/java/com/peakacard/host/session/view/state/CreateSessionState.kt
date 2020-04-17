package com.peakacard.host.session.view.state

sealed class CreateSessionState {

    object GeneratingSessionId : CreateSessionState()
    class SessionIdGenerated(val sessionId: String) : CreateSessionState()
    object Error : CreateSessionState()
}
