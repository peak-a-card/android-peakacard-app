package com.peakacard.result.domain.model

import com.peakacard.card.domain.model.Card

sealed class GetFinalVotingResultResponse {

  class GroupedParticipantVote(val result: Map<Vote, List<ParticipantVote>>) : GetFinalVotingResultResponse()

  sealed class Error {
    object NoParticipants : Error()
    object Unspecified : Error()
  }
}

class ParticipantVote(val participantName: String, val card: Card)
sealed class Vote(open val score: Float) {
  class Mode(override val score: Float): Vote(score)
  class Regular(override val score: Float): Vote(score)
}
