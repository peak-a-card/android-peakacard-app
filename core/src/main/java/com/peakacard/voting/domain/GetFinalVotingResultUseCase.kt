package com.peakacard.voting.domain

import com.peakacard.result.domain.model.GetVotingResultResponse
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.voting.data.repository.VotingRepository
import com.peakacard.core.Either
import com.peakacard.participant.domain.ParticipantsVotingService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest

class GetFinalVotingResultUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository,
    private val participantsVotingService: ParticipantsVotingService
) {

    suspend fun getFinalVotingResult(): Either<GetVotingResultResponse.Error, List<GetVotingResultResponse.Success.Voted>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            Either.Left(GetVotingResultResponse.Error.Unspecified)
        } else {
            val currentVoting = votingRepository.getCurrentVoting()
            if (currentVoting == null) {
                Either.Left(GetVotingResultResponse.Error.Unspecified)
            } else {
                val mapLatest = participantsVotingService.combineParticipantsWithVotingResults(
                    sessionId,
                    currentVoting
                ).mapLatest {
                    it.fold(
                        { error -> Either.Left(error) },
                        { votingResultsSuccess ->
                            val votedResults =
                                votingResultsSuccess.filterIsInstance<GetVotingResultResponse.Success.Voted>()
                            Either.Right(votedResults)
                        }
                    )
                }
                mapLatest.first()
            }
        }
    }
}
