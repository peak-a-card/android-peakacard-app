package com.peakacard.app.cards.data.repository

import com.peakacard.app.cards.data.datasource.InMemoryCardsDataSource
import com.peakacard.app.cards.data.model.CardDataModel
import com.peakacard.app.cards.domain.model.CardDomainModel

class CardsRepository(private val inMemoryCardsDataSource: InMemoryCardsDataSource) {

    fun getCards(): List<CardDomainModel> = inMemoryCardsDataSource.cards.map { it.toDomainModel() }
}

private fun CardDataModel.toDomainModel() = CardDomainModel(score)
