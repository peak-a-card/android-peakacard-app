package com.peakacard.app.voting.data.datasource.remote.model

sealed class VotingResponse {
    data class Success(val votingTitle: String) : VotingResponse()
    sealed class Error : VotingResponse() {
        object NoVotingStarted : Error()
        object RemoteException : Error()
    }
}
