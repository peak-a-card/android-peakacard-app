package com.peakacard.app.voting.domain

import com.peakacard.core.Either
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import com.peakacard.voting.domain.model.GetVotingError
import com.peakacard.voting.domain.model.Voting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class GetStartedVotingUseCase(
  private val votingRepository: VotingRepository,
  private val sessionRepository: SessionRepository
) {

  suspend fun getStartedVoting(): Flow<Either<GetVotingError, Voting>> {
    val sessionId = sessionRepository.getCurrentSession()
    return if (sessionId == null) {
      flowOf(Either.Left(GetVotingError.NoSessionJoined))
    } else {
      votingRepository.getStartedVotation(sessionId)
        .onEach {
          it.fold(
            {
              // DO NOTHING
            },
            { voting -> votingRepository.saveCurrentVoting(voting) }
          )
        }
    }
  }
}
