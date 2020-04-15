package com.peakacard.app.result.domain.model

sealed class GetVotingResultResponse(open val participantName: String) {

    sealed class Success(participantName: String) : GetVotingResultResponse(participantName) {
        class Voted(
            override val participantName: String,
            val score: Float
        ) : Success(participantName)

        class Unvoted(override val participantName: String) :
            Success(participantName)
    }

    sealed class Error {
        object Unspecified : Error()
    }
}
