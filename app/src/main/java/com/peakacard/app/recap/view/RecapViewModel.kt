package com.peakacard.app.recap.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.recap.view.state.RecapState
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.voting.domain.GetFinalVotingResultUseCase
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class RecapViewModel(
  private val getFinalVotingResultUseCase: GetFinalVotingResultUseCase,
  private val getStartedVotingUseCase: GetStartedVotingUseCase,
  private val cardUiModelMapper: CardUiModelMapper
) : PeakViewModel<RecapState>() {

  fun getVotingResult() {
    viewModelScope.launch {
      getFinalVotingResultUseCase.getFinalVotingResult().fold(
        { error ->
          Timber.e("Error getting final participants votes. Error $error")
          when (error) {
            GetVotingResultResponse.Error.NoParticipants -> {
              state.offer(RecapState.VotationsLoaded(emptyList()))
            }
            GetVotingResultResponse.Error.Unspecified -> {
              state.offer(RecapState.Error)
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
          state.offer(RecapState.VotationsLoaded(participantUiModels))
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
            else -> state.offer(RecapState.Error)
          }
        }, { voting ->
          Timber.d("Voting title: ${voting.title}")
          if (!state.isClosedForSend) {
            state.offer(RecapState.VotingStarted(voting.title))
          }
          state.close()
        })
      }
    }
  }
}
