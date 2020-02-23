package com.peakacard.app.view

import com.peakacard.app.view.state.CardsViewState

interface CardsView {
    fun updateState(state: CardsViewState)
}