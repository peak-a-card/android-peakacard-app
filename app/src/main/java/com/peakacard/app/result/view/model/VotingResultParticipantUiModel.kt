package com.peakacard.app.result.view.model

sealed class VotingResultParticipantUiModel(open val name: String) {

    class Voted(override val name: String, val score: Float) : VotingResultParticipantUiModel(name)
    class Waiting(override val name: String) : VotingResultParticipantUiModel(name)
}
