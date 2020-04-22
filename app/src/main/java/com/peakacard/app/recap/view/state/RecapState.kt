package com.peakacard.app.recap.view.state

import com.peakacard.app.result.view.model.VotingResultParticipantUiModel

sealed class RecapState {
  data class VotationsLoaded(val uiModels: List<VotingResultParticipantUiModel.Voted>) :
    RecapState()

  class VotingStarted(val title: String) : RecapState()

  object Error : RecapState()
}
