package com.peakacard.app.cards.view

import com.peakacard.app.cards.view.state.CardsViewState

interface CardsView {
    fun updateState(state: CardsViewState)
}