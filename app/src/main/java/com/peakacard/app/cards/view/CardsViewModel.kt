package com.peakacard.app.cards.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.model.Card
import com.peakacard.app.cards.view.state.CardsViewState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CardsViewModel(private val getCardsUseCase: GetCardsUseCase) : ViewModel() {

    private val cardsViewState: BroadcastChannel<CardsViewState> = ConflatedBroadcastChannel()
    private var currentState: CardsViewState = CardsViewState.Loading

    fun bindView(view: CardsView) {
        viewModelScope.launch {
            cardsViewState
                .asFlow()
                .onStart { cardsViewState.offer(currentState) }
                .collect { view.updateState(it) }
        }
    }

    fun getCards() {
        viewModelScope.launch {
            val cards = getCardsUseCase.getCards().map { Card.fromScore(it.score) }
            delay(2000)
            currentState = CardsViewState.Loaded(cards)
            cardsViewState.offer(currentState)
        }
    }
}