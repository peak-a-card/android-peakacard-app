package com.peakacard.app.voting.view.state

import com.peakacard.app.voting.view.model.SessionParticipant

sealed class WaitParticipantState {
    data class ParticipantsAlreadyJoined(val participants: List<SessionParticipant>) :
        WaitParticipantState()

    data class ParticipantJoined(val participant: SessionParticipant) : WaitParticipantState()
    object Error : WaitParticipantState()
}
