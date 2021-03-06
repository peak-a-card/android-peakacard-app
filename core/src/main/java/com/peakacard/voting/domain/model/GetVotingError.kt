package com.peakacard.voting.domain.model

sealed class GetVotingError {
  object NoVotingStarted : GetVotingError()
  object NoVotingEnded : GetVotingError()
  object Unspecified : GetVotingError()
  object NoSessionJoined : GetVotingError()
}
