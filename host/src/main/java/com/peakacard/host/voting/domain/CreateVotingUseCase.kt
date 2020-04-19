package com.peakacard.host.voting.domain

import com.peakacard.core.Either
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import com.peakacard.voting.domain.model.CreateVotingResponse

class CreateVotingUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun createVoting(title: String): Either<CreateVotingResponse.Error, CreateVotingResponse.Success> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            Either.Left(CreateVotingResponse.Error.NoSessionId)
        } else {
            votingRepository.createVoting(sessionId, title)
        }
    }
}
