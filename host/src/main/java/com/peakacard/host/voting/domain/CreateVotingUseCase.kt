package com.peakacard.host.voting.domain

import com.peakacard.core.Either
import com.peakacard.host.session.data.repository.SessionRepository
import com.peakacard.host.voting.data.repository.VotingRepository
import com.peakacard.host.voting.domain.model.CreateVotingResponse

class CreateVotingUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun createVoting(title: String): Either<CreateVotingResponse.Error, CreateVotingResponse.Success> {
        val sessionId = sessionRepository.getSessionId()
        return if (sessionId == null) {
            Either.Left(CreateVotingResponse.Error.NoSessionId)
        } else {
            votingRepository.createVoting(sessionId, title)
        }
    }
}
