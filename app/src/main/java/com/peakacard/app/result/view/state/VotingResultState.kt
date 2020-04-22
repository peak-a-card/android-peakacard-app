package com.peakacard.app.result.view.state

import com.peakacard.app.result.view.model.VotingResultParticipantUiModel

sealed class VotingResultState {

  data class ParticipantsLoaded(val uiModels: List<VotingResultParticipantUiModel>) :
    VotingResultState()

  object Error : VotingResultState()
}
