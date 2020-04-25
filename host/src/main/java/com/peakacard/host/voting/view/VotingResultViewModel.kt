package com.peakacard.host.voting.view

import androidx.lifecycle.viewModelScope
import com.peakacard.card.view.model.mapper.CardUiModelMapper
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.voting.view.model.GroupedVoteParticipantUiModel
import com.peakacard.host.voting.view.state.VotingResultState
import com.peakacard.result.domain.model.GetFinalVotingResultResponse
import com.peakacard.voting.domain.GetFinalVotingResultUseCase
import kotlinx.coroutines.launch
import timber.log.Timber

class VotingResultViewModel(
  private val getFinalVotingResultUseCase: GetFinalVotingResultUseCase,
  private val cardUiModelMapper: CardUiModelMapper
) :
  PeakViewModel<VotingResultState>() {

  fun getVotingResult() {
    viewModelScope.launch {
      getFinalVotingResultUseCase.getFinalVotingResult().fold(
        { error ->
          Timber.e("Error getting final participants votes. Error $error")
          when (error) {
            GetFinalVotingResultResponse.Error.NoParticipants -> state.offer(VotingResultState.VotationsLoaded(emptyList()))
            GetFinalVotingResultResponse.Error.Unspecified -> state.offer(VotingResultState.Error)
          }
        },
        { participants ->
          Timber.d("Got final participants votes successfully")
          val groupedVoteParticipantUiModels = participants.result.keys.map { score ->
            val participantsVote = participants.result.getOrElse(score, { emptyList() })
            val participantNames = participantsVote.map { participantVote -> participantVote.participantName }
            GroupedVoteParticipantUiModel(cardUiModelMapper.map(participantsVote.first().card), participantNames)
          }

          state.offer(VotingResultState.VotationsLoaded(groupedVoteParticipantUiModels))
        }
      )
    }
  }
}
