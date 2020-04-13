package com.peakacard.app.cards.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.state.CardsState
import com.peakacard.app.session.domain.LeaveSessionUseCase
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsViewModel(
    private val getCardsUseCase: GetCardsUseCase,
    private val cardUiModelMapper: CardUiModelMapper,
    private val leaveSessionUseCase: LeaveSessionUseCase
) : ViewModel() {

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
            val cards = getCardsUseCase.getCards().map { cardUiModelMapper.map(it) }
            currentState = CardsState.Loaded(cards)
            cardsState.offer(currentState)
        }
    }

    fun leaveSession() {
        viewModelScope.launch {
            leaveSessionUseCase.leaveSession().fold(
                {
                    Timber.e("Error leaving session")
                },
                {
                    Timber.d("Session left successfully!")
                    cardsState.offer(CardsState.VotingLeft)
                }
            )
        }
    }
}
