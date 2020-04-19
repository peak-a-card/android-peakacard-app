package com.peakacard.host.voting.view.state

import com.peakacard.host.voting.view.model.VotedParticipantUiModel

sealed class WaitingVotesState {

    data class ParticipantsVoteLoaded(val participants: List<VotedParticipantUiModel>) :
        WaitingVotesState()

    object EndingVote : WaitingVotesState()

    object VoteEnded : WaitingVotesState()

    object Error : WaitingVotesState()
}
