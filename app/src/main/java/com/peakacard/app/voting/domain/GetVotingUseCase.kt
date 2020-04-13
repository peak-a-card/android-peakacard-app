package com.peakacard.app.voting.domain

import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.model.GetVotingError
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach

class GetVotingUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun getVoting(): Flow<Either<GetVotingError, Voting>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            flowOf(Either.Left(GetVotingError.NoSessionJoined))
        } else {
            votingRepository.getVotation(sessionId)
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
