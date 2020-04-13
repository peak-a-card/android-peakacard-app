package com.peakacard.app.cards.data.repository

import com.peakacard.app.cards.data.datasource.local.CardsLocalDataSource
import com.peakacard.app.cards.data.datasource.local.model.CardDataModel
import com.peakacard.app.cards.domain.model.Card

class CardsRepository(private val cardsLocalDataSource: CardsLocalDataSource) {

    fun getCards(): List<Card> = cardsLocalDataSource.cards.map { it.toDomainModel() }
}

private fun CardDataModel.toDomainModel() = Card(score)
