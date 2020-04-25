package com.peakacard.voting.view.model

import com.peakacard.card.view.model.CardUiModel

sealed class VoteParticipantStatusUiModel(open val name: String) {

  class Voted(override val name: String, val card: CardUiModel) : VoteParticipantStatusUiModel(name)
  class Waiting(override val name: String) : VoteParticipantStatusUiModel(name)
}
