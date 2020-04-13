package com.peakacard.app.cards.data.repository

import com.peakacard.app.cards.data.datasource.InMemoryCardsDataSource
import com.peakacard.app.cards.data.model.CardDataModel
import com.peakacard.app.cards.domain.model.Card

class CardsRepository(private val inMemoryCardsDataSource: InMemoryCardsDataSource) {

    fun getCards(): List<Card> = inMemoryCardsDataSource.cards.map { it.toDomainModel() }
}

private fun CardDataModel.toDomainModel() = Card(score)
