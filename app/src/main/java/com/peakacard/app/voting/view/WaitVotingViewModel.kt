package com.peakacard.app.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
import com.peakacard.app.voting.view.state.WaitVotingState
import com.peakacard.core.view.PeakViewModel
import com.peakacard.participant.domain.model.ParticipantError
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class WaitVotingViewModel(
  private val getStartedVotingUseCase: GetStartedVotingUseCase,
  private val getSessionParticipantUseCase: GetSessionParticipantUseCase,
  private val leaveSessionUseCase: LeaveSessionUseCase
) : PeakViewModel<WaitVotingState>() {

  fun listenForVotingToStart() {
    state.offer(WaitVotingState.WaitingVotingStart)
    viewModelScope.launch {
      getStartedVotingUseCase.getStartedVoting().collectLatest {
        it.fold(
          { error ->
            Timber.e("Error waiting voting. Error: $error")
            when (error) {
              GetVotingError.NoVotingStarted -> state.offer(WaitVotingState.WaitingVotingStart)
              else -> state.offer(WaitVotingState.Error)
            }
          },
          { voting ->
            Timber.d("Voting title: ${voting.title}")
            state.offer(WaitVotingState.VotingStarted(voting.title))
          })
      }
    }
  }

  fun listenParticipantsToJoin() {
    viewModelScope.launch {
      getSessionParticipantUseCase.getSessionParticipant().collectLatest {
        it.fold(
          { error ->
            Timber.e("Error waiting for participant. Error: $error")
            when (error) {
              ParticipantError.NoParticipants -> state.offer(WaitVotingState.WaitParticipantState.ParticipantsLoaded(emptyList()))
              ParticipantError.Unspecified -> state.offer(WaitVotingState.WaitParticipantState.Error)
            }
          },
          { participants ->
            Timber.d("$participants")
            val participantUiModels = participants.map { participant -> SessionParticipantUiModel(participant.name) }
            state.offer(WaitVotingState.WaitParticipantState.ParticipantsLoaded(participantUiModels))
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
          state.offer(WaitVotingState.VotingLeft)
        }
      )
    }
  }
}
