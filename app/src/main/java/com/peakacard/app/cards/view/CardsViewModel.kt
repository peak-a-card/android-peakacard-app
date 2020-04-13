package com.peakacard.app.cards.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.model.CardUiModel
import com.peakacard.app.cards.view.state.CardsState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CardsViewModel(private val getCardsUseCase: GetCardsUseCase) : ViewModel() {

    private val cardsState: BroadcastChannel<CardsState> = ConflatedBroadcastChannel()
    private var currentState: CardsState = CardsState.Loading

    fun bindView(view: CardsView) {
        viewModelScope.launch {
            cardsState
                .asFlow()
                .onStart { cardsState.offer(currentState) }
                .collect { view.updateState(it) }
        }
    }

    fun getCards() {
        viewModelScope.launch {
            val cards = getCardsUseCase.getCards().map { CardUiModel.fromScore(it.score) }
            currentState = CardsState.Loaded(cards)
            cardsState.offer(currentState)
        }
    }
}
