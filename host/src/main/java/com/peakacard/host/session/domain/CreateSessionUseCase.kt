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
                        Either.Right(CreateSessionResponse.Success("1"))
                    }
                }
            },
            { success ->
                val maxId = success.ids.map { it.toInt() }.max()
                if (maxId == null) {
                    Either.Left(CreateSessionResponse.Error)
                } else {
                    val nextId = maxId + 1
                    Either.Right(CreateSessionResponse.Success(nextId.toString()))
                }
            }
        ).run {
            when (this) {
                is Either.Left -> this
                is Either.Right -> createSessionById(item)
            }
        }
    }

    private suspend fun createSessionById(item: CreateSessionResponse.Success) =
        sessionRepository.createSession(item.id).also {
            sessionRepository.saveSessionId(item.id)
        }
}
