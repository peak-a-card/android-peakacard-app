package com.peakacard.app.recap.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.recap.view.state.RecapState
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.result.domain.model.GetFinalVotingResultResponse
import com.peakacard.result.domain.model.Vote
import com.peakacard.voting.domain.GetFinalVotingResultUseCase
import com.peakacard.voting.domain.model.GetVotingError
import com.peakacard.voting.view.model.GroupedVoteParticipantUiModel
import com.peakacard.voting.view.model.VoteResultCard
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
      getFinalVotingResultUseCase.getFinalVotingResult()
        .fold(
          { error ->
            Timber.e("Error getting final participants votes. Error $error")
            when (error) {
              GetFinalVotingResultResponse.Error.NoParticipants -> state.offer(RecapState.VotationsLoaded(emptyList()))
              GetFinalVotingResultResponse.Error.Unspecified -> state.offer(RecapState.Error)
            }
          },
          { participants ->
            Timber.d("Got final participants votes successfully")
            val groupedVoteParticipantUiModels: List<GroupedVoteParticipantUiModel> = participants.result.keys.map { score ->
              val participantsVote = participants.result.getOrElse(score, { emptyList() })
              val participantNames = participantsVote.map { participantVote -> participantVote.participantName }
              val voteResultCard = when (score) {
                is Vote.Mode -> VoteResultCard.Mode(cardUiModelMapper.map(participantsVote.first().card))
                is Vote.Regular -> VoteResultCard.Regular(cardUiModelMapper.map(participantsVote.first().card))
              }
              GroupedVoteParticipantUiModel(voteResultCard, participantNames)
            }

            state.offer(RecapState.VotationsLoaded(groupedVoteParticipantUiModels))
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
          state.offer(RecapState.VotingStarted(voting.title))
        })
      }
    }
  }
}
