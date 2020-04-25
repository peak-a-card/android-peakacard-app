package com.peakacard.app.result.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.result.domain.GetVotingResultUseCase
import com.peakacard.voting.view.model.VoteParticipantStatusUiModel
import com.peakacard.app.result.view.state.VotingResultState
import com.peakacard.app.voting.domain.GetEndedVotingUseCase
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.voting.domain.model.GetVotingError
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(
  private val getVotingResultUseCase: GetVotingResultUseCase,
  private val getEndedVotingUseCase: GetEndedVotingUseCase,
  private val cardUiModelMapper: CardUiModelMapper
) : PeakViewModel<VotingResultState>() {

  fun listenParticipantsVote() {
    viewModelScope.launch {
      getVotingResultUseCase.getVotingResult().collectLatest {
        it.fold(
          { error ->
            Timber.e("Error listening participants votes. Error $error")
            when (error) {
              GetVotingResultResponse.Error.NoParticipants -> state.offer(VotingResultState.ParticipantsLoaded(emptyList()))
              GetVotingResultResponse.Error.Unspecified -> state.offer(VotingResultState.Error)
            }
          },
          { participants ->
            Timber.d("Got participants votes successfully")
            val participantStatusUiModels: List<VoteParticipantStatusUiModel> =
              participants.map { participant ->
                when (participant) {
                  is GetVotingResultResponse.Success.Voted -> {
                    Timber.d("Participant ${participant.participantName} voted ${participant.card.score}")
                    VoteParticipantStatusUiModel.Voted(participant.participantName, cardUiModelMapper.map(participant.card))
                  }
                  is GetVotingResultResponse.Success.Unvoted -> {
                    Timber.d("Participant ${participant.participantName} has not yet voted")
                    VoteParticipantStatusUiModel.Waiting(participant.participantName)
                  }
                }
              }
            state.offer(VotingResultState.ParticipantsLoaded(participantStatusUiModels))
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
              GetVotingError.NoVotingEnded -> state.offer(VotingResultState.EndedVotingState.WaitingVotingEnd)
              else -> state.offer(VotingResultState.EndedVotingState.Error)
            }
          },
          { voting ->
            Timber.d("Voting title: ${voting.title}")
            state.offer(VotingResultState.EndedVotingState.VotingEnded(voting.title))
          }
        )
      }
    }
  }
}
