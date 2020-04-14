package com.peakacard.app.result.view.state

import com.peakacard.app.voting.view.model.SessionParticipantUiModel

sealed class VotingResultState {

    data class ParticipantsLoaded(val participantUiModels: List<SessionParticipantUiModel>) :
        VotingResultState()

    object Error : VotingResultState()
}
