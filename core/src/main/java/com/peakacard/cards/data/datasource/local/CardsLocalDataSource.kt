package com.peakacard.cards.data.datasource.local

import com.peakacard.cards.data.datasource.local.model.CardDataModel

class CardsLocalDataSource {

  private val cards: List<CardDataModel> by lazy {
    listOf(
      CardDataModel(0f),
      CardDataModel(0.5f),
      CardDataModel(1f),
      CardDataModel(2f),
      CardDataModel(3f),
      CardDataModel(5f),
      CardDataModel(8f),
      CardDataModel(13f),
      CardDataModel(20f),
      CardDataModel(40f),
      CardDataModel(100f),
      CardDataModel(999f),
      CardDataModel(-1f),
      CardDataModel(-2f)
    )
  }

  fun getAllCards(): List<CardDataModel> {
    return cards
  }

  fun getCard(score: Float): CardDataModel? {
    return cards.firstOrNull { it.score == score }
  }
}
