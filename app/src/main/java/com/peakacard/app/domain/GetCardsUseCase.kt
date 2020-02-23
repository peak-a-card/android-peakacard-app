package com.peakacard.app.domain

import com.peakacard.app.data.repository.CardsRepository
import com.peakacard.app.domain.model.CardDomainModel

class GetCardsUseCase(private val cardsRepository: CardsRepository) {

    fun getCards(): List<CardDomainModel> = cardsRepository.getCards()
}