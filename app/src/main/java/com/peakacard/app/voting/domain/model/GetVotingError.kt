package com.peakacard.app.voting.domain.model

sealed class GetVotingError {
    object NoVotingStarted : GetVotingError()
    object Unspecified : GetVotingError()
    object NoSessionJoined : GetVotingError()
}