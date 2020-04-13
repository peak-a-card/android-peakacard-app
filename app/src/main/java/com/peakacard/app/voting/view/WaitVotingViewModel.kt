package com.peakacard.app.voting.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import com.peakacard.app.voting.domain.GetVotingUseCase
import com.peakacard.app.voting.view.model.SessionParticipant
import com.peakacard.app.voting.view.state.WaitParticipantState
import com.peakacard.app.voting.view.state.WaitVotingState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class WaitVotingViewModel(
    private val getVotingUseCase: GetVotingUseCase,
    private val getSessionParticipantUseCase: GetSessionParticipantUseCase
) : ViewModel() {

    private val waitVotingState: BroadcastChannel<WaitVotingState> = ConflatedBroadcastChannel()
    private val waitParticipantState: BroadcastChannel<WaitParticipantState> =
        ConflatedBroadcastChannel()

    fun bindView(view: WaitVotingView) {
        viewModelScope.launch {
            waitVotingState
                .asFlow()
                .collect { view.updateVotingState(it) }
        }
        viewModelScope.launch {
            waitParticipantState
                .asFlow()
                .collect { view.updateParticipantState(it) }
        }
    }

    fun listenForVotingToStart() {
        waitVotingState.offer(WaitVotingState.WaitingVotingStart)
        viewModelScope.launch {
            getVotingUseCase.getVoting().collect {
                it.fold({ error ->
                    Timber.e("Error waiting voting. Error: $error")
                    waitVotingState.offer(WaitVotingState.Error)
                }, { voting ->
                    Timber.d("Voting title: ${voting.title}")
                    waitVotingState.offer(WaitVotingState.VotingStarted(voting.title))
                })
            }
        }
    }

    fun listenParticipantsToJoin() {
        viewModelScope.launch {
            getSessionParticipantUseCase.getSessionParticipant().collect {
                it.fold({ error ->
                    Timber.e("Error waiting for participant. Error: $error")
                    waitParticipantState.offer(WaitParticipantState.Error)
                }, { participant ->
                    Timber.d("$participant")
                    waitParticipantState.offer(
                        WaitParticipantState.ParticipantJoined(
                            SessionParticipant(participant.name)
                        )
                    )
                })
            }
        }
    }
}
