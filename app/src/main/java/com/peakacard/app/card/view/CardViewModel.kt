package com.peakacard.app.card.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.domain.SendVoteUseCase
import com.peakacard.card.view.model.CardUiModel
import com.peakacard.app.card.view.model.mapper.CardModelMapper
import com.peakacard.app.card.view.state.CardState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class CardViewModel(
    private val sendVoteUseCase: SendVoteUseCase,
    private val cardModelMapper: CardModelMapper
) : ViewModel() {

    private val cardState: BroadcastChannel<CardState> = ConflatedBroadcastChannel()

    fun bindView(view: CardView) {
        viewModelScope.launch {
            cardState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun sendCard(cardUiModel: CardUiModel) {
        cardState.offer(CardState.Sending)

        viewModelScope.launch {
            sendVoteUseCase.sendVote(cardModelMapper.map(cardUiModel)).fold(
                {
                    Timber.e("Error voting")
                },
                {
                    Timber.d("Voting success!")
                    cardState.offer(CardState.Sent)
                }
            )
        }
    }
}
