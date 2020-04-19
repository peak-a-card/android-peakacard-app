package com.peakacard.voting.domain.model

sealed class EndVoteResponse {
    object Success : EndVoteResponse()
    sealed class Error : EndVoteResponse() {
        object NoSessionId : Error()
        object NoVoteRunning : Error()
        object Unspecified : Error()
    }
}
