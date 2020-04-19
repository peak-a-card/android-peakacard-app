package com.peakacard.app.recap.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import com.peakacard.app.recap.view.state.RecapState
import com.peakacard.app.result.domain.GetFinalVotingResultUseCase
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class RecapViewModel(
    private val getFinalVotingResultUseCase: GetFinalVotingResultUseCase,
    private val getStartedVotingUseCase: GetStartedVotingUseCase,
    private val cardUiModelMapper: CardUiModelMapper
) : ViewModel() {

    private val recapState: BroadcastChannel<RecapState> = ConflatedBroadcastChannel()

    fun bindView(view: RecapView) {
        viewModelScope.launch {
            recapState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun getVotingResult() {
        viewModelScope.launch {
            getFinalVotingResultUseCase.getFinalVotingResult().fold(
                { error ->
                    Timber.e("Error getting final participants votes. Error $error")
                    when (error) {
                        GetVotingResultResponse.Error.NoParticipants -> {
                            recapState.offer(RecapState.VotationsLoaded(emptyList()))
                        }
                        GetVotingResultResponse.Error.Unspecified -> {
                            recapState.offer(RecapState.Error)
                        }
                    }
                },
                { participants ->
                    Timber.d("Got final participants votes successfully")
                    val participantUiModels: List<VotingResultParticipantUiModel.Voted> =
                        participants.map { participant ->
                            Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                            VotingResultParticipantUiModel.Voted(
                                participant.participantName,
                                cardUiModelMapper.map(participant.card)
                            )
                        }
                    recapState.offer(RecapState.VotationsLoaded(participantUiModels))
                }
            )
        }
    }

    fun listenForVotingToStart() {
        viewModelScope.launch {
            getStartedVotingUseCase.getStartedVoting().collectLatest {
                it.fold({ error ->
                    Timber.e("Error waiting voting. Error: $error")
                    when (error) {
                        GetVotingError.NoVotingStarted -> {
                            // DO NOTHING
                        }
                        else -> recapState.offer(RecapState.Error)
                    }
                }, { voting ->
                    Timber.d("Voting title: ${voting.title}")
                    if (!recapState.isClosedForSend) {
                        recapState.offer(RecapState.VotingStarted(voting.title))
                    }
                    recapState.close()
                })
            }
        }
    }
}
