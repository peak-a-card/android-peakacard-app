package com.peakacard.cards.data.repository

import com.peakacard.cards.data.datasource.local.CardsLocalDataSource
import com.peakacard.cards.data.datasource.local.model.CardDataModel
import com.peakacard.card.domain.model.Card

class CardsRepository(private val cardsLocalDataSource: CardsLocalDataSource) {

    fun getCards(): List<Card> = cardsLocalDataSource.getAllCards().map { it.toDomainModel() }

    fun getCard(score: Float): Card? = cardsLocalDataSource.getCard(score)?.toDomainModel()
}

private fun CardDataModel.toDomainModel() =
    Card(score)