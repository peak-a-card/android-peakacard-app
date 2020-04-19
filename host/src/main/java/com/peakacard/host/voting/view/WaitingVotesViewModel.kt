package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.domain.EndVoteUseCase
import com.peakacard.host.voting.domain.GetParticipantsVoteUseCase
import com.peakacard.host.voting.view.model.VotedParticipantUiModel
import com.peakacard.host.voting.view.state.WaitingVotesState
import com.peakacard.result.domain.model.GetVotingResultResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class WaitingVotesViewModel(
    private val getParticipantsVoteUseCase: GetParticipantsVoteUseCase,
    private val endVoteUseCase: EndVoteUseCase
) : PeakViewModel<WaitingVotesState>() {

    fun listenParticipantsVote() {
        viewModelScope.launch {
            getParticipantsVoteUseCase.getParticipantsVote().collectLatest {
                it.fold(
                    { error ->
                        Timber.e("Error getting voting results")
                        when (error) {
                            GetVotingResultResponse.Error.NoParticipants -> {
                                state.offer(WaitingVotesState.ParticipantsVoteLoaded(emptyList()))
                            }
                            GetVotingResultResponse.Error.Unspecified -> {
                                state.offer(WaitingVotesState.Error)
                            }
                        }
                    },
                    { participants ->
                        Timber.d("${participants.size} participants voted")
                        val participantUiModels: List<VotedParticipantUiModel> =
                            participants.map { participant ->
                                Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                                VotedParticipantUiModel(participant.participantName)
                            }
                        state.offer(WaitingVotesState.ParticipantsVoteLoaded(participantUiModels))
                    }
                )
            }
        }
    }

    fun endVote() {
        state.offer(WaitingVotesState.EndingVote)
        viewModelScope.launch {
            endVoteUseCase.endVote().fold(
                {
                    Timber.e("Error ending vote")
                    state.offer(WaitingVotesState.Error)
                },
                {
                    Timber.d("Ended vote success")
                    state.offer(WaitingVotesState.VoteEnded)
                }
            )
        }
    }

}
