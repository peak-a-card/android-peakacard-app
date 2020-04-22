package com.peakacard.app.cards.domain

import com.peakacard.card.domain.model.Card
import com.peakacard.cards.data.repository.CardsRepository

class GetCardsUseCase(private val cardsRepository: CardsRepository) {

  fun getCards(): List<Card> = cardsRepository.getCards()
}
