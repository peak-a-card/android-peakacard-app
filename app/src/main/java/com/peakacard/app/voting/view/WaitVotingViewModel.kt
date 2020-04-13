package com.peakacard.app.voting.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.participant.domain.GetAllSessionParticipantsUseCase
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.app.voting.domain.GetVotingUseCase
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
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
    private val getAllSessionParticipantsUseCase: GetAllSessionParticipantsUseCase,
    private val getSessionParticipantUseCase: GetSessionParticipantUseCase,
    private val leaveSessionUseCase: LeaveSessionUseCase
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

    fun getAlreadyJoinedParticipants() {
        viewModelScope.launch {
            getAllSessionParticipantsUseCase.getAllSessionParticipants().fold(
                { error ->
                    Timber.e("Error waiting for participant. Error: $error")
                    waitParticipantState.offer(WaitParticipantState.Error)
                },
                { participants ->
                    Timber.d("$participants")
                    val sessionParticipants = participants.map { SessionParticipantUiModel(it.name) }
                    waitParticipantState.offer(
                        WaitParticipantState.ParticipantsAlreadyJoined(
                            sessionParticipants
                        )
                    )
                }
            )
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
                            SessionParticipantUiModel(participant.name)
                        )
                    )
                })
            }
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
                    waitVotingState.offer(WaitVotingState.VotingLeft)
                }
            )
        }
    }
}
