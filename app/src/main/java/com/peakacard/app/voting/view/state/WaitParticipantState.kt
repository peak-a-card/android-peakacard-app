package com.peakacard.app.voting.view.state

import com.peakacard.app.voting.view.model.SessionParticipantUiModel

sealed class WaitParticipantState {
    data class ParticipantsAlreadyJoined(val participantUiModels: List<SessionParticipantUiModel>) :
        WaitParticipantState()

    data class ParticipantJoined(val participantUiModel: SessionParticipantUiModel) : WaitParticipantState()
    object Error : WaitParticipantState()
}
