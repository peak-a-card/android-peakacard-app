package com.peakacard.app.card.view.state

sealed class CardState {
  object Sending : CardState()
  object Sent : CardState()
}
