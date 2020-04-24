package com.peakacard.app.cards.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.state.CardsState
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class CardsViewModel(
  private val getCardsUseCase: GetCardsUseCase,
  private val cardUiModelMapper: CardUiModelMapper,
  private val leaveSessionUseCase: LeaveSessionUseCase
) : PeakViewModel<CardsState>() {

  fun getCards() {
    state.offer(CardsState.Loading)
    viewModelScope.launch {
      val cards = getCardsUseCase.getCards().map { cardUiModelMapper.map(it) }
      state.offer(CardsState.Loaded(cards))
    }
  }

  fun leaveSession() {
    viewModelScope.launch {
      leaveSessionUseCase.leaveSession().fold(
        {
          Timber.e("Error leaving session")
          state.offer(CardsState.Error.CouldNotLeaveVoting)
        },
        {
          Timber.d("Session left successfully!")
          state.offer(CardsState.VotingLeft)
        }
      )
    }
  }
}
