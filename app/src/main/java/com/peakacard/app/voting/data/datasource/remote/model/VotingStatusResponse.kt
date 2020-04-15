package com.peakacard.app.voting.data.datasource.remote.model

sealed class VotingStatusResponse {
    data class Success(val votingTitle: String) : VotingStatusResponse()
    sealed class Error : VotingStatusResponse() {
        object NoVotingStarted : Error()
        object NoVotingEnded : Error()
        object RemoteException : Error()
    }
}
