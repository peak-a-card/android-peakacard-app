package com.peakacard.app.result.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.result.domain.GetVotingResultUseCase
import com.peakacard.app.result.domain.model.GetVotingResultResponse
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.app.result.view.state.VotingResultState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(private val getVotingResultUseCase: GetVotingResultUseCase) :
    ViewModel() {

    private val votingResultState: BroadcastChannel<VotingResultState> = ConflatedBroadcastChannel()

    fun bindView(view: VotingResultView) {
        viewModelScope.launch {
            votingResultState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun listenParticipantsVote() {
        viewModelScope.launch {
            getVotingResultUseCase.getVotingResult().collectLatest {
                it.fold(
                    { error ->
                        Timber.e("Error listening participants votes. Error $error")
                        votingResultState.offer(VotingResultState.Error)
                    },
                    { participants ->
                        Timber.d("Got participants votes successfully")
                        val participantUiModels =
                            participants.map { participant ->
                                when (participant) {
                                    is GetVotingResultResponse.Success.Voted -> {
                                        Timber.d("Participant ${participant.participantName} voted ${participant.score}")
                                        VotingResultParticipantUiModel.Voted(
                                            participant.participantName,
                                            participant.score
                                        )
                                    }
                                    is GetVotingResultResponse.Success.Unvoted -> {
                                        Timber.d("Participant ${participant.participantName} has not yet voted")
                                        VotingResultParticipantUiModel.Waiting(
                                            participant.participantName
                                        )
                                    }
                                }
                            }
                        votingResultState.offer(
                            VotingResultState.ParticipantsLoaded(participantUiModels)
                        )
                    }
                )
            }
        }
    }
}
