package com.peakacard.app.cards.view.state

import com.peakacard.app.cards.view.model.CardUiModel

sealed class CardsViewState {
    object Loading : CardsViewState()
    class Loaded(val cardUiModels: List<CardUiModel>) : CardsViewState()
    object Empty : CardsViewState()
    object Error : CardsViewState()
}
