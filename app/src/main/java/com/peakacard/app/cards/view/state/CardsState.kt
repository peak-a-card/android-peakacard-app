package com.peakacard.app.cards.view.state

import com.peakacard.card.view.model.CardUiModel

sealed class CardsState {
    object Loading : CardsState()
    class Loaded(val cardUiModels: List<CardUiModel>) : CardsState()
    object Empty : CardsState()
    object Error : CardsState()
    object VotingLeft : CardsState()
}
