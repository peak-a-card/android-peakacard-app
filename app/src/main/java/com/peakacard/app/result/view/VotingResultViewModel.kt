package com.peakacard.app.result.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import com.peakacard.app.result.domain.GetVotingResultUseCase
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.app.result.view.state.EndedVotingState
import com.peakacard.app.result.view.state.VotingResultState
import com.peakacard.app.voting.domain.GetEndedVotingUseCase
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(
    private val getVotingResultUseCase: GetVotingResultUseCase,
    private val getEndedVotingUseCase: GetEndedVotingUseCase,
    private val cardUiModelMapper: CardUiModelMapper
) : ViewModel() {

    private val endedVotingState: BroadcastChannel<EndedVotingState> = ConflatedBroadcastChannel()
    private val votingResultState: BroadcastChannel<VotingResultState> = ConflatedBroadcastChannel()

    fun bindView(view: VotingResultView) {
        viewModelScope.launch {
            votingResultState
                .asFlow()
                .collect { view.updateState(it) }
        }
        viewModelScope.launch {
            endedVotingState
                .asFlow()
                .collect { view.updateVotingState(it) }
        }
    }

    fun listenParticipantsVote() {
        viewModelScope.launch {
            getVotingResultUseCase.getVotingResult().collectLatest {
                it.fold(
                    { error ->
                        Timber.e("Error listening participants votes. Error $error")
                        votingResultState.offer(VotingResultState.Error)
                    },
                    { participants ->
                        Timber.d("Got participants votes successfully")
                        val participantUiModels: List<VotingResultParticipantUiModel> =
                            participants.map { participant ->
                                when (participant) {
                                    is GetVotingResultResponse.Success.Voted -> {
                                        Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                                        VotingResultParticipantUiModel.Voted(
                                            participant.participantName,
                                            cardUiModelMapper.map(participant.card)
                                        )
                                    }
                                    is GetVotingResultResponse.Success.Unvoted -> {
                                        Timber.d("Participant ${participant.participantName} has not yet voted")
                                        VotingResultParticipantUiModel.Waiting(
                                            participant.participantName
                                        )
                                    }
                                }
                            }
                        votingResultState.offer(
                            VotingResultState.ParticipantsLoaded(participantUiModels)
                        )
                    }
                )
            }
        }
    }

    fun listenForVotingToEnd() {
        viewModelScope.launch {
            getEndedVotingUseCase.getEndedVoting().collectLatest {
                it.fold(
                    { error ->
                        Timber.e("Error waiting voting. Error: $error")
                        when (error) {
                            GetVotingError.NoVotingEnded -> {
                                endedVotingState.offer(EndedVotingState.WaitingVotingEnd)
                            }
                            else -> endedVotingState.offer(EndedVotingState.Error)
                        }
                    },
                    { voting ->
                        Timber.d("Voting title: ${voting.title}")
                        if (!endedVotingState.isClosedForSend) {
                            endedVotingState.offer(EndedVotingState.VotingEnded(voting.title))
                        }
                        endedVotingState.close()
                    }
                )
            }
        }
    }
}
