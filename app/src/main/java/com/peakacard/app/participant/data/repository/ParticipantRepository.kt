package com.peakacard.app.participant.data.repository

import com.peakacard.app.participant.data.datasource.remote.ParticipantRemoteDataSource
import com.peakacard.app.participant.domain.model.Participant
import com.peakacard.app.participant.domain.model.ParticipantError
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParticipantRepository(private val participantRemoteDataSource: ParticipantRemoteDataSource) {

    suspend fun getSessionParticipant(sessionId: String): Flow<Either<ParticipantError, Participant>> {
        return participantRemoteDataSource.getSessionParticipant(sessionId)
            .map { participantResponse ->
                participantResponse.fold(
                    { Either.Left(ParticipantError) },
                    {
                        with(it.participant) {
                            Either.Right(Participant(email, name))
                        }
                    }
                )
            }
    }
}
