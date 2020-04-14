package com.peakacard.app.result.domain

import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.model.GetVotingError
import com.peakacard.app.voting.domain.model.ParticipantsVotation
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetVotingResultUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun getVotingResult(): Flow<Either<GetVotingError, Voting>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            flowOf(Either.Left(GetVotingError.Unspecified))
        } else {
            val currentVoting = votingRepository.getCurrentVoting()
            if (currentVoting == null) {
                flowOf(Either.Left(GetVotingError.Unspecified))
            } else {
                val participantsVotation = ParticipantsVotation(sessionId, currentVoting.title)
                votingRepository.getParticipantsVotation(participantsVotation)
            }
        }
    }
}
