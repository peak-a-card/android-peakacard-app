package com.peakacard.app.voting.view.state

import com.peakacard.app.voting.view.model.SessionParticipantUiModel

sealed class WaitParticipantState {
  data class ParticipantsLoaded(val participantUiModels: List<SessionParticipantUiModel>) :
    WaitParticipantState()

  object Error : WaitParticipantState()
}
