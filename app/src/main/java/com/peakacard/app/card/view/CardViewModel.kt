package com.peakacard.app.card.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.domain.SendVoteUseCase
import com.peakacard.app.card.view.model.mapper.CardModelMapper
import com.peakacard.app.card.view.state.CardState
import com.peakacard.card.view.model.CardUiModel
import com.peakacard.core.view.PeakViewModel
import kotlinx.coroutines.launch
import timber.log.Timber

class CardViewModel(
  private val sendVoteUseCase: SendVoteUseCase,
  private val cardModelMapper: CardModelMapper
) : PeakViewModel<CardState>() {

  fun sendCard(cardUiModel: CardUiModel) {
    state.offer(CardState.Sending)

    viewModelScope.launch {
      sendVoteUseCase.sendVote(cardModelMapper.map(cardUiModel)).fold(
        {
          Timber.e("Error voting")
        },
        {
          Timber.d("Voting success!")
          state.offer(CardState.Sent)
        }
      )
    }
  }
}
