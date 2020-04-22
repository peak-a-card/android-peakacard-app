package com.peakacard.host.voting.domain

import com.peakacard.core.Either
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import com.peakacard.voting.domain.model.EndVoteResponse

class EndVoteUseCase(
  private val sessionRepository: SessionRepository,
  private val votingRepository: VotingRepository
) {

  suspend fun endVote(): Either<EndVoteResponse.Error, EndVoteResponse.Success> {
    val sessionId = sessionRepository.getCurrentSession()
    return if (sessionId == null) {
      Either.Left(EndVoteResponse.Error.NoSessionId)
    } else {
      val currentVoting = votingRepository.getCurrentVoting()
      if (currentVoting == null) {
        Either.Left(EndVoteResponse.Error.NoVoteRunning)
      } else {
        votingRepository.endVote(sessionId, currentVoting.title)
      }
    }
  }
}
