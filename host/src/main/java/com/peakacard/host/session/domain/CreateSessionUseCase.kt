package com.peakacard.host.session.domain

import com.peakacard.core.Either
import com.peakacard.host.session.data.repository.SessionRepository
import com.peakacard.host.session.domain.model.CreateSessionResponse
import com.peakacard.host.session.domain.model.GetAllSessionIdsResponse

class CreateSessionUseCase(private val sessionRepository: SessionRepository) {

    suspend fun createSession(): Either<CreateSessionResponse.Error, CreateSessionResponse.Success> {
        return sessionRepository.getAllSessionIds().fold(
            { error ->
                when (error) {
                    GetAllSessionIdsResponse.Error.Unspecified -> {
                        Either.Left(CreateSessionResponse.Error)
                    }
                    GetAllSessionIdsResponse.Error.NoSessionStarted -> {
                        Either.Right(CreateSessionResponse.Success(1))
                    }
                }
            },
            { success ->
                val maxId = success.ids.map { it.toInt() }.max()
                if (maxId == null) {
                    Either.Left(CreateSessionResponse.Error)
                } else {
                    Either.Right(CreateSessionResponse.Success(maxId + 1))
                }
            }
        ).run {
            when (this) {
                is Either.Left -> this
                is Either.Right -> sessionRepository.createSession(item.id)
            }
        }
    }
}
