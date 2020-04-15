package com.peakacard.app.result.domain

import com.peakacard.app.result.domain.model.GetVotingResultResponse
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetVotingResultUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository,
    private val participantsVotingService: ParticipantsVotingService
) {

    suspend fun getVotingResult(): Flow<Either<GetVotingResultResponse.Error, List<GetVotingResultResponse.Success>>> {
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
