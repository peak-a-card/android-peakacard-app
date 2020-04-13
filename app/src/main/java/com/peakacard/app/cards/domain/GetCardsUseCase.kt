package com.peakacard.app.cards.domain

import com.peakacard.app.cards.data.repository.CardsRepository
import com.peakacard.app.cards.domain.model.Card

class GetCardsUseCase(private val cardsRepository: CardsRepository) {

    fun getCards(): List<Card> = cardsRepository.getCards()
}
