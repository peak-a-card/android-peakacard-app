package com.peakacard.app.voting.domain

import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.model.GetVotingError
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetEndedVotingUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun getEndedVoting(): Flow<Either<GetVotingError, Voting>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            flowOf(Either.Left(GetVotingError.NoSessionJoined))
        } else {
            val currentVoting = votingRepository.getCurrentVoting()
            if (currentVoting == null) {
                flowOf(Either.Left(GetVotingError.NoVotingStarted))
            } else {
                votingRepository.getEndedVotation(sessionId, currentVoting.title)
            }
        }
    }
}
