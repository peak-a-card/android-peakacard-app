package com.peakacard.app.voting.view.state

import com.peakacard.app.voting.view.model.SessionParticipantUiModel

sealed class WaitVotingState {
  object WaitingVotingStart : WaitVotingState()
  data class VotingStarted(val title: String) : WaitVotingState()
  object VotingLeft : WaitVotingState()
  object Error : WaitVotingState()

  sealed class WaitParticipantState : WaitVotingState() {
    data class ParticipantsLoaded(val participantUiModels: List<SessionParticipantUiModel>) : WaitParticipantState()
    object Error : WaitParticipantState()
  }
}
