package com.peakacard.app.card.view

import com.peakacard.app.card.view.state.CardState

interface CardView {
    fun updateState(state: CardState)
}
