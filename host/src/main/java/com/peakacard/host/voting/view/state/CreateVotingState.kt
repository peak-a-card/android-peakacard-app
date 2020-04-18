package com.peakacard.host.voting.view.state

sealed class CreateVotingState {

    object CreatingVoting : CreateVotingState()
    object VotingCreated : CreateVotingState()

    sealed class Error : CreateVotingState() {
        object TitleRequiredError : Error()
        object NoSessionFound : Error()
        object Unspecified : Error()
    }
}
