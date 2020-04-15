package com.peakacard.app.result.domain.model

import com.peakacard.app.card.domain.model.Card

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
        object Unspecified : Error()
    }
}
