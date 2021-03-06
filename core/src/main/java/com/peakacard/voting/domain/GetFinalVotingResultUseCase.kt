package com.peakacard.voting.domain

import com.peakacard.core.Either
import com.peakacard.core.extensions.statistics.mode
import com.peakacard.participant.domain.ParticipantsVotingService
import com.peakacard.result.domain.model.GetFinalVotingResultResponse
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.result.domain.model.ParticipantVote
import com.peakacard.result.domain.model.Vote
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

class GetFinalVotingResultUseCase(
  private val votingRepository: VotingRepository,
  private val sessionRepository: SessionRepository,
  private val participantsVotingService: ParticipantsVotingService
) {

  suspend fun getFinalVotingResult(): Either<GetFinalVotingResultResponse.Error, GetFinalVotingResultResponse.GroupedParticipantVote> {
    val sessionId = sessionRepository.getCurrentSession()
    return if (sessionId == null) {
      Either.Left(GetFinalVotingResultResponse.Error.Unspecified)
    } else {
      val currentVoting = votingRepository.getCurrentVoting()
      if (currentVoting == null) {
        Either.Left(GetFinalVotingResultResponse.Error.Unspecified)
      } else {
        participantsVotingService.combineParticipantsWithVotingResults(
          sessionId,
          currentVoting
        ).mapLatest {
          it.fold(
            { error ->
              when (error) {
                GetVotingResultResponse.Error.NoParticipants -> Either.Left(GetFinalVotingResultResponse.Error.NoParticipants)
                GetVotingResultResponse.Error.Unspecified -> Either.Left(GetFinalVotingResultResponse.Error.Unspecified)
              }
            },
            { votingResultsSuccess ->
              val votedResults = votingResultsSuccess.filterIsInstance<GetVotingResultResponse.Success.Voted>()
              val groupedVotedResults = votedResults
                .map { votingResultVoted -> ParticipantVote(votingResultVoted.participantName, votingResultVoted.card) }
                .sortedBy { participantVote -> participantVote.card.score }
                .groupBy { participantVote -> participantVote.card.score }

              val mode = votedResults.map { success -> success.card.score }.mode()

              val result = groupedVotedResults.map { entry ->
                if (mode.contains(entry.key)) {
                  Vote.Mode(entry.key) to entry.value
                } else {
                  Vote.Regular(entry.key) to entry.value
                }
              }.toMap()

              Either.Right(GetFinalVotingResultResponse.GroupedParticipantVote(result))
            }
          )
        }.first()
      }
    }
  }
}
