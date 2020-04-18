package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.domain.CreateVotingUseCase
import com.peakacard.host.voting.domain.model.CreateVotingResponse
import com.peakacard.host.voting.view.state.CreateVotingState
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateVotingViewModel(private val createVotingUseCase: CreateVotingUseCase) :
    PeakViewModel<CreateVotingState>() {

    fun createVoting(title: String) {
        if (title.isEmpty()) {
            state.offer(CreateVotingState.Error.TitleRequiredError)
            return
        }
        state.offer(CreateVotingState.CreatingVoting)
        viewModelScope.launch {
            createVotingUseCase.createVoting(title).fold(
                { error ->
                    Timber.e("Error creating voting")
                    when (error) {
                        CreateVotingResponse.Error.NoSessionId -> {
                            state.offer(CreateVotingState.Error.NoSessionFound)
                        }
                        CreateVotingResponse.Error.Unspecified -> {
                            state.offer(CreateVotingState.Error.Unspecified)
                        }
                    }
                },
                { state.offer(CreateVotingState.VotingCreated) }
            )
        }
    }

}
