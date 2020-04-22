package com.peakacard.app.voting.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
import com.peakacard.app.voting.view.state.WaitParticipantState
import com.peakacard.app.voting.view.state.WaitVotingState
import com.peakacard.participant.domain.model.ParticipantError
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class WaitVotingViewModel(
  private val getStartedVotingUseCase: GetStartedVotingUseCase,
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
      getStartedVotingUseCase.getStartedVoting().collectLatest {
        it.fold({ error ->
          Timber.e("Error waiting voting. Error: $error")
          when (error) {
            GetVotingError.NoVotingStarted -> {
              waitVotingState.offer(WaitVotingState.WaitingVotingStart)
            }
            else -> waitVotingState.offer(WaitVotingState.Error)
          }
        }, { voting ->
          Timber.d("Voting title: ${voting.title}")
          if (!waitVotingState.isClosedForSend) {
            waitVotingState.offer(WaitVotingState.VotingStarted(voting.title))
          }
          waitVotingState.close()
        })
      }
    }
  }

  fun listenParticipantsToJoin() {
    viewModelScope.launch {
      getSessionParticipantUseCase.getSessionParticipant().collectLatest {
        it.fold({ error ->
          Timber.e("Error waiting for participant. Error: $error")
          when (error) {
            ParticipantError.NoParticipants -> {
              waitParticipantState.offer(
                WaitParticipantState.ParticipantsLoaded(emptyList())
              )
            }
            ParticipantError.Unspecified -> {
              waitParticipantState.offer(WaitParticipantState.Error)
            }
          }
        }, { participants ->
          Timber.d("$participants")
          val participantUiModels =
            participants.map { participant -> SessionParticipantUiModel(participant.name) }
          waitParticipantState.offer(
            WaitParticipantState.ParticipantsLoaded(
              participantUiModels
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
