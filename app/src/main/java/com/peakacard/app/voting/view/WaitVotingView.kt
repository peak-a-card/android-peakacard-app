package com.peakacard.app.voting.view

import com.peakacard.app.voting.view.state.WaitVotingState

interface WaitVotingView {
    fun updateState(state: WaitVotingState)
}
