package com.peakacard.app.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.domain.GetCardsUseCase
import com.peakacard.app.view.model.Card
import com.peakacard.app.view.state.CardsViewState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CardsViewModel(private val getCardsUseCase: GetCardsUseCase) : ViewModel() {

    private val cardsViewState: BroadcastChannel<CardsViewState> = ConflatedBroadcastChannel()

    fun bindView(view: CardsView) {
        viewModelScope.launch {
            cardsViewState
                .asFlow()
                .onStart { emit(CardsViewState.Loading) }
                .collect { view.updateState(it) }
        }
    }

    fun getCards() {
        viewModelScope.launch {
            val cards = getCardsUseCase.getCards().map { Card.fromScore(it.score) }
            delay(2000)
            cardsViewState.offer(CardsViewState.Loaded(cards))
        }
    }
}