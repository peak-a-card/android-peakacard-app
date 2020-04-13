package com.peakacard.app.participant.domain

import com.peakacard.app.participant.data.repository.ParticipantRepository
import com.peakacard.app.participant.domain.model.Participant
import com.peakacard.app.participant.domain.model.ParticipantError
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.core.Either

class GetAllSessionParticipantsUseCase(
    private val participantRepository: ParticipantRepository,
    private val sessionRepository: SessionRepository
) {

    suspend fun getAllSessionParticipants(): Either<ParticipantError, List<Participant>> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            Either.Left(ParticipantError)
        } else {
            participantRepository.getAllSessionParticipants(sessionId)
        }
    }
}
