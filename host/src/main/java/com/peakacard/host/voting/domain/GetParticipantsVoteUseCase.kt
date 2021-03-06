package com.peakacard.host.voting.domain

import com.peakacard.core.Either
import com.peakacard.participant.domain.ParticipantsVotingService
import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetParticipantsVoteUseCase(
  private val votingRepository: VotingRepository,
  private val sessionRepository: SessionRepository,
  private val participantsVotingService: ParticipantsVotingService
) {

  suspend fun getParticipantsVote(): Flow<Either<GetVotingResultResponse.Error, List<GetVotingResultResponse.Success>>> {
    val sessionId = sessionRepository.getCurrentSession()
    return if (sessionId == null) {
      flowOf(Either.Left(GetVotingResultResponse.Error.Unspecified))
    } else {
      val currentVoting = votingRepository.getCurrentVoting()
      if (currentVoting == null) {
        flowOf(Either.Left(GetVotingResultResponse.Error.Unspecified))
      } else {
        participantsVotingService.combineParticipantsWithVotingResults(
          sessionId,
          currentVoting
        )
      }
    }
  }
}
