package com.peakacard.host.voting.view.state

import com.peakacard.host.voting.view.model.VotingResultParticipantUiModel

sealed class VotingResultState {
    data class VotationsLoaded(val uiModels: List<VotingResultParticipantUiModel>) :
        VotingResultState()

    object Error : VotingResultState()
}
