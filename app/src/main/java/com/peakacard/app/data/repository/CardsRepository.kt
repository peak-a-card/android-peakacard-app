package com.peakacard.app.data.repository

import com.peakacard.app.data.datasource.InMemoryCardsDataSource
import com.peakacard.app.data.model.CardDataModel
import com.peakacard.app.domain.model.CardDomainModel

class CardsRepository(private val inMemoryCardsDataSource: InMemoryCardsDataSource) {

    fun getCards(): List<CardDomainModel> = inMemoryCardsDataSource.cards.map { it.toDomainModel() }
}

private fun CardDataModel.toDomainModel() = CardDomainModel(score)
