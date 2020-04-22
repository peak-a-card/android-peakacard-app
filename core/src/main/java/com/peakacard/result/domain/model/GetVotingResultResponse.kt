package com.peakacard.result.domain.model

import com.peakacard.card.domain.model.Card

sealed class GetVotingResultResponse(open val participantName: String) {

  sealed class Success(participantName: String) : GetVotingResultResponse(participantName) {
    class Voted(
      override val participantName: String,
      val card: Card
    ) : Success(participantName)

    class Unvoted(override val participantName: String) :
      Success(participantName)
  }

  sealed class Error {
    object NoParticipants : Error()
    object Unspecified : Error()
  }
}
