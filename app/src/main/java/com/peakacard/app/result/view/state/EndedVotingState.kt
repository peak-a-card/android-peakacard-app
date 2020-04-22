package com.peakacard.app.result.view.state

sealed class EndedVotingState {
  object WaitingVotingEnd : EndedVotingState()
  data class VotingEnded(val title: String) : EndedVotingState()
  object Error : EndedVotingState()
}
