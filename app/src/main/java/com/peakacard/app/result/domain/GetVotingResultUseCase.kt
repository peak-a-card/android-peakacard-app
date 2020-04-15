package com.peakacard.app.result.domain

import com.peakacard.app.participant.data.repository.ParticipantRepository
import com.peakacard.app.participant.domain.model.Participant
import com.peakacard.app.participant.domain.model.ParticipantError
import com.peakacard.app.result.domain.model.GetParticipantsVotationError
import com.peakacard.app.result.domain.model.GetParticipantsVotationResponse
import com.peakacard.app.result.domain.model.GetVotingResultResponse
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.model.ParticipantsVotation
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class GetVotingResultUseCase(
    private val votingRepository: VotingRepository,
    private val sessionRepository: SessionRepository,
    private val participantRepository: ParticipantRepository
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
                zipParticipantsWithVotingResults(sessionId, currentVoting)
            }
        }
    }

    private suspend fun zipParticipantsWithVotingResults(
        sessionId: String,
        currentVoting: Voting
    ): Flow<Either<GetVotingResultResponse.Error, List<GetVotingResultResponse.Success>>> {
        val participantsVotation = ParticipantsVotation(sessionId, currentVoting.title)
        return participantRepository.getSessionParticipants(sessionId)
            .combine(votingRepository.getParticipantsVotation(participantsVotation), combineFlows())
    }

    private fun combineFlows(): suspend (Either<ParticipantError, List<Participant>>, Either<GetParticipantsVotationError, List<GetParticipantsVotationResponse>>) -> Either<GetVotingResultResponse.Error.Unspecified, List<GetVotingResultResponse.Success>> {
        return { eitherParticipants, eitherVotingResult ->
            eitherParticipants.fold(
                { Either.Left(GetVotingResultResponse.Error.Unspecified) },
                { participants ->
                    Either.Right(getVotingResultSuccessList(participants, eitherVotingResult))
                }
            )
        }
    }

    private fun getVotingResultSuccessList(
        participants: List<Participant>,
        eitherVotingResult: Either<GetParticipantsVotationError, List<GetParticipantsVotationResponse>>
    ): List<GetVotingResultResponse.Success> {
        return participants.map { participant ->
            eitherVotingResult.fold(
                { GetVotingResultResponse.Success.Unvoted(participant.name) },
                { votingResults -> getParticipantVote(votingResults, participant) }
            )
        }
    }

    private fun getParticipantVote(
        votingResults: List<GetParticipantsVotationResponse>,
        participant: Participant
    ): GetVotingResultResponse.Success {
        val votingResult = votingResults.firstOrNull { it.uid == participant.uid }
        return if (votingResult == null) {
            GetVotingResultResponse.Success.Unvoted(participant.name)
        } else {
            GetVotingResultResponse.Success.Voted(participant.name, votingResult.score)
        }
    }
}
