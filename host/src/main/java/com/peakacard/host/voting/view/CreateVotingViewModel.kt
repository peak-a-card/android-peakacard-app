package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.view.state.CreateVotingState
import kotlinx.coroutines.launch

class CreateVotingViewModel : PeakViewModel<CreateVotingState>() {

    fun createVoting(title: String) {
        if (title.isEmpty()) {
            state.offer(CreateVotingState.Error.TitleRequiredError)
            return
        }
        state.offer(CreateVotingState.CreatingVoting)
        viewModelScope.launch {

        }
    }

}
