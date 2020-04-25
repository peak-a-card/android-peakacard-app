package com.peakacard.host.voting.view.state

import com.peakacard.host.voting.view.model.GroupedVoteParticipantUiModel

sealed class VotingResultState {
  data class VotationsLoaded(val uiModels: List<GroupedVoteParticipantUiModel>) : VotingResultState()

  object Error : VotingResultState()
}
