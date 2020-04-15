package com.peakacard.app.result.view.model

import com.peakacard.app.card.view.model.CardUiModel

sealed class VotingResultParticipantUiModel(open val name: String) {

    class Voted(override val name: String, val card: CardUiModel) : VotingResultParticipantUiModel(name)
    class Waiting(override val name: String) : VotingResultParticipantUiModel(name)
}
