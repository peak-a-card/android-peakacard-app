package com.peakacard.host.voting.view.state

import com.peakacard.voting.view.model.VoteParticipantStatusUiModel

sealed class WaitingVotesState {

  data class ParticipantsLoaded(val uiModels: List<VoteParticipantStatusUiModel>) : WaitingVotesState()

  object EndingVote : WaitingVotesState()

  data class VoteEnded(val title: String) : WaitingVotesState()

  object Error : WaitingVotesState()
}
