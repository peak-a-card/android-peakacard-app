package com.peakacard.card.view.model.mapper

import com.peakacard.card.domain.model.Card
import com.peakacard.card.view.model.CardUiModel

class CardUiModelMapper {
  fun map(card: Card): CardUiModel {
    return when (card.score) {
      0f -> CardUiModel.ZERO
      0.5f -> CardUiModel.HALF
      1f -> CardUiModel.ONE
      2f -> CardUiModel.TWO
      3f -> CardUiModel.THREE
      5f -> CardUiModel.FIVE
      8f -> CardUiModel.EIGHT
      13f -> CardUiModel.THIRTEEN
      20f -> CardUiModel.TWENTY
      40f -> CardUiModel.FORTY
      100f -> CardUiModel.HUNDRED
      999f -> CardUiModel.INFINITE
      -1f -> CardUiModel.UNKNOWN
      -2f -> CardUiModel.COFFEE
      else -> CardUiModel.UNKNOWN
    }
  }
}
