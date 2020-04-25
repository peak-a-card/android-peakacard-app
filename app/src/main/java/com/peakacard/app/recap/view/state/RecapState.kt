package com.peakacard.app.recap.view.state

import com.peakacard.voting.view.model.GroupedVoteParticipantUiModel

sealed class RecapState {
  data class VotationsLoaded(val uiModels: List<GroupedVoteParticipantUiModel>) : RecapState()

  class VotingStarted(val title: String) : RecapState()

  object Error : RecapState()
}
