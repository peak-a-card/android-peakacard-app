package com.peakacard.participant.data.repository

import com.peakacard.core.Either
import com.peakacard.participant.data.datasource.remote.ParticipantRemoteDataSource
import com.peakacard.participant.data.datasource.remote.model.ParticipantsResponse
import com.peakacard.participant.domain.model.Participant
import com.peakacard.participant.domain.model.ParticipantError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ParticipantRepository(private val participantRemoteDataSource: ParticipantRemoteDataSource) {

  suspend fun getSessionParticipants(sessionId: String):
    Flow<Either<ParticipantError, List<Participant>>> {
    return participantRemoteDataSource.getSessionParticipants(sessionId)
      .map { participantResponse ->
        participantResponse.fold(
          { error ->
            when (error) {
              ParticipantsResponse.Error.NoParticipants -> {
                Either.Left(ParticipantError.NoParticipants)
              }
              ParticipantsResponse.Error.RemoteException -> {
                Either.Left(ParticipantError.Unspecified)
              }
            }
          },
          { success ->
            val participants = success.participants.map {
              Participant(it.id, it.email, it.name)
            }
            Either.Right(participants)
          }
        )
      }
  }
}
