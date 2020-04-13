package com.peakacard.app.cards.data.datasource.local

import com.peakacard.app.cards.data.datasource.local.model.CardDataModel

class CardsLocalDataSource {

    val cards: List<CardDataModel>
        get() = listOf(
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
