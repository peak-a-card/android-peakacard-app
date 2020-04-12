package com.peakacard.app.participant.domain

import com.peakacard.app.participant.data.repository.ParticipantRepository
import com.peakacard.app.participant.domain.model.Participant
import com.peakacard.app.participant.domain.model.ParticipantError
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class GetSessionParticipantUseCase(
    private val participantRepository: ParticipantRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun getSessionParticipant(): Flow<Either<ParticipantError, Participant>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            flowOf(Either.Left(ParticipantError))
        } else {
            participantRepository.getSessionParticipant(sessionId)
        }
    }
}
