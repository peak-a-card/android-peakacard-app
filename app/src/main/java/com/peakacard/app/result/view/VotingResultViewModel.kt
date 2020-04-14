package com.peakacard.app.result.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import com.peakacard.app.result.domain.GetVotingResultUseCase
import com.peakacard.app.result.view.state.VotingResultState
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(
    private val getSessionParticipantUseCase: GetSessionParticipantUseCase,
    private val getVotingResultUseCase: GetVotingResultUseCase
) : ViewModel() {

    private val votingResultState: BroadcastChannel<VotingResultState> = ConflatedBroadcastChannel()

    fun bindView(view: VotingResultView) {
        viewModelScope.launch {
            votingResultState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun getParticipants() {
        viewModelScope.launch {
            getSessionParticipantUseCase.getSessionParticipant().collectLatest {
                it.fold({ error ->
                    Timber.e("Error waiting for participant. Error: $error")
                    votingResultState.offer(VotingResultState.Error)
                }, { participants ->
                    Timber.d("$participants")
                    val participantUiModels =
                        participants.map { participant -> SessionParticipantUiModel(participant.name) }
                    votingResultState.offer(
                        VotingResultState.ParticipantsLoaded(
                            participantUiModels
                        )
                    )
                })
            }
        }
    }

    fun listenParticipantsVote() {
        viewModelScope.launch {
            getVotingResultUseCase.getVotingResult().collectLatest {
                it.fold(
                    { error ->
                        Timber.e("Error listening participants votes. Error $error")
                    },
                    {
                        Timber.d("Got participants votes successfully")
                    }
                )
            }
        }
    }
}
