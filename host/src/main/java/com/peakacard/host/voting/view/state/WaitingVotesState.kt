package com.peakacard.host.voting.view.state

import com.peakacard.host.voting.view.model.VotedParticipantUiModel

sealed class WaitingVotesState {

    data class ParticipantsVoteLoaded(val participants: List<VotedParticipantUiModel>) :
        WaitingVotesState()

    object EndingVote : WaitingVotesState()

    data class VoteEnded(val title: String) : WaitingVotesState()

    object Error : WaitingVotesState()
}
