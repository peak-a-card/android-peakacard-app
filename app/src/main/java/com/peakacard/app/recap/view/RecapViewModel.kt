package com.peakacard.app.recap.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import com.peakacard.app.recap.view.state.RecapState
import com.peakacard.app.result.domain.GetFinalVotingResultUseCase
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class RecapViewModel(
    private val getFinalVotingResultUseCase: GetFinalVotingResultUseCase,
    private val cardUiModelMapper: CardUiModelMapper
) : ViewModel() {

    private val recapState: BroadcastChannel<RecapState> = ConflatedBroadcastChannel()

    fun bindView(view: RecapView) {
        viewModelScope.launch {
            recapState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun getVotingResult() {
        viewModelScope.launch {
            getFinalVotingResultUseCase.getFinalVotingResult().fold(
                { error ->
                    Timber.e("Error getting final participants votes. Error $error")
                    recapState.offer(RecapState.Error)
                },
                { participants ->
                    Timber.d("Got final participants votes successfully")
                    val participantUiModels: List<VotingResultParticipantUiModel.Voted> =
                        participants.map { participant ->
                            Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                            VotingResultParticipantUiModel.Voted(
                                participant.participantName,
                                cardUiModelMapper.map(participant.card)
                            )
                        }
                    recapState.offer(RecapState.VotationsLoaded(participantUiModels))
                }
            )
        }
    }
}
