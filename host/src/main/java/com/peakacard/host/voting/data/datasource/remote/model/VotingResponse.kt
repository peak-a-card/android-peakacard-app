package com.peakacard.host.voting.data.datasource.remote.model

sealed class VotingResponse {

    object Success : VotingResponse()
    object RemoteError : VotingResponse()
}
