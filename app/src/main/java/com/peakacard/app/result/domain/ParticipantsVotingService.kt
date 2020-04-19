package com.peakacard.app.result.domain

import com.peakacard.app.cards.data.repository.CardsRepository
import com.peakacard.participant.data.repository.ParticipantRepository
import com.peakacard.participant.domain.model.Participant
import com.peakacard.participant.domain.model.ParticipantError
import com.peakacard.app.result.domain.model.GetParticipantsVotationError
import com.peakacard.app.result.domain.model.GetParticipantsVotationResponse
import com.peakacard.app.result.domain.model.GetVotingResultResponse
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.model.ParticipantsVotation
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ParticipantsVotingService(
    private val votingRepository: VotingRepository,
    private val participantRepository: ParticipantRepository,
    private val cardsRepository: CardsRepository
) {

    suspend fun combineParticipantsWithVotingResults(
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
            val card = cardsRepository.getCard(votingResult.score)
            if (card == null) {
                GetVotingResultResponse.Success.Unvoted(participant.name)
            } else {
                GetVotingResultResponse.Success.Voted(participant.name, card)
            }
        }
    }
}
