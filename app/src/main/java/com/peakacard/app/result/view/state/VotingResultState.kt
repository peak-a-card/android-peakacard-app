package com.peakacard.app.result.view.state

import com.peakacard.voting.view.model.VoteParticipantStatusUiModel

sealed class VotingResultState {

  data class ParticipantsLoaded(val uiModels: List<VoteParticipantStatusUiModel>) : VotingResultState()
  object Error : VotingResultState()

  sealed class EndedVotingState : VotingResultState() {
    object WaitingVotingEnd : EndedVotingState()
    data class VotingEnded(val title: String) : EndedVotingState()
    object Error : EndedVotingState()
  }
}
