package com.peakacard.app.result.view

import com.peakacard.app.result.view.state.VotingResultState

interface VotingResultView {
    fun updateState(state: VotingResultState)
}