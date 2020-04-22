package com.peakacard.app.cards.view

import com.peakacard.app.cards.view.state.CardsState

interface CardsView {
  fun updateState(state: CardsState)
}
