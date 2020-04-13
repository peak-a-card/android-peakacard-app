package com.peakacard.app.voting.view.state

sealed class WaitVotingState {
    object WaitingVotingStart : WaitVotingState()
    data class VotingStarted(val title: String) : WaitVotingState()
    object VotingLeft : WaitVotingState()
    object Error : WaitVotingState()
}
