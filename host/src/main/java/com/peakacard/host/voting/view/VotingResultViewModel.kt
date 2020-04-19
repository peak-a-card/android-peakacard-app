package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.view.model.VotingResultParticipantUiModel
import com.peakacard.host.voting.view.state.VotingResultState
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.voting.domain.GetFinalVotingResultUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(
    private val getFinalVotingResultUseCase: GetFinalVotingResultUseCase,
    private val cardUiModelMapper: CardUiModelMapper
) :
    PeakViewModel<VotingResultState>() {

    fun getVotingResult() {
        viewModelScope.launch {
            getFinalVotingResultUseCase.getFinalVotingResult().fold(
                { error ->
                    Timber.e("Error getting final participants votes. Error $error")
                    when (error) {
                        GetVotingResultResponse.Error.NoParticipants -> {
                            state.offer(VotingResultState.VotationsLoaded(emptyList()))
                        }
                        GetVotingResultResponse.Error.Unspecified -> {
                            state.offer(VotingResultState.Error)
                        }
                    }
                },
                { participants ->
                    Timber.d("Got final participants votes successfully")
                    val participantUiModels: List<VotingResultParticipantUiModel> =
                        participants.map { participant ->
                            Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                            VotingResultParticipantUiModel(
                                participant.participantName,
                                cardUiModelMapper.map(participant.card)
                            )
                        }
                    state.offer(VotingResultState.VotationsLoaded(participantUiModels))
                }
            )
        }
    }
}
