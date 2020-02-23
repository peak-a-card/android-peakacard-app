package com.peakacard.app.cards.view.state

import com.peakacard.app.cards.view.model.Card

sealed class CardsViewState {
    object Loading : CardsViewState()
    class Loaded(val cards: List<Card>) : CardsViewState()
    object Empty : CardsViewState()
    object Error : CardsViewState()
}