package com.peakacard.app.voting.view

import com.peakacard.app.voting.view.state.WaitParticipantState
import com.peakacard.app.voting.view.state.WaitVotingState

interface WaitVotingView {
    fun updateVotingState(state: WaitVotingState)
    fun updateParticipantState(state: WaitParticipantState)
}
