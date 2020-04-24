package com.peakacard.app.cards.view.state

import com.peakacard.card.view.model.CardUiModel

sealed class CardsState {
  object Loading : CardsState()
  class Loaded(val cardUiModels: List<CardUiModel>) : CardsState()
  object Empty : CardsState()
  sealed class Error : CardsState() {
    object Unspecified : Error()
    object CouldNotLeaveVoting : Error()
  }
  object VotingLeft : CardsState()
}
