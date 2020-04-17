package com.peakacard.host.session.domain

import com.peakacard.core.Either
import com.peakacard.host.session.data.repository.SessionRepository
import com.peakacard.host.session.domain.model.GenerateSessionCodeResponse
import com.peakacard.host.session.domain.model.GetAllSessionIdsResponse

class GenerateSessionCodeUseCase(private val sessionRepository: SessionRepository) {

    suspend fun generateSessionCode(): Either<GenerateSessionCodeResponse.Error, GenerateSessionCodeResponse.Success> {
        return sessionRepository.getAllSessionIds().fold(
            { error ->
                when (error) {
                    GetAllSessionIdsResponse.Error.Unspecified -> {
                        Either.Left(GenerateSessionCodeResponse.Error)
                    }
                    GetAllSessionIdsResponse.Error.NoSessionStarted -> {
                        Either.Right(GenerateSessionCodeResponse.Success(1))
                    }
                }
            },
            { success ->
                val maxId = success.ids.map { it.toInt() }.max()
                if (maxId == null) {
                    Either.Left(GenerateSessionCodeResponse.Error)
                } else {
                    Either.Right(GenerateSessionCodeResponse.Success(maxId + 1))
                }
            }
        )
    }
}
