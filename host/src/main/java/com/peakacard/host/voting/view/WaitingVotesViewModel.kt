package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.domain.EndVoteUseCase
import com.peakacard.host.voting.domain.GetParticipantsVoteUseCase
import com.peakacard.host.voting.view.model.VotedParticipantUiModel
import com.peakacard.host.voting.view.state.WaitingVotesState
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.voting.view.model.VoteParticipantStatusUiModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

class WaitingVotesViewModel(
  private val getParticipantsVoteUseCase: GetParticipantsVoteUseCase,
  private val endVoteUseCase: EndVoteUseCase,
  private val cardUiModelMapper: CardUiModelMapper
) : PeakViewModel<WaitingVotesState>() {

  fun listenParticipantsVote() {
    viewModelScope.launch {
      getParticipantsVoteUseCase.getParticipantsVote().collectLatest {
        it.fold(
          { error ->
            Timber.e("Error getting voting results")
            when (error) {
              GetVotingResultResponse.Error.NoParticipants -> {
                state.offer(WaitingVotesState.ParticipantsLoaded(emptyList()))
              }
              GetVotingResultResponse.Error.Unspecified -> {
                state.offer(WaitingVotesState.Error)
              }
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
            state.offer(WaitingVotesState.ParticipantsLoaded(participantStatusUiModels))
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
          Timber.d("Ended vote ${it.title} successfully")
          state.offer(WaitingVotesState.VoteEnded(it.title))
        }
      )
    }
  }

}
