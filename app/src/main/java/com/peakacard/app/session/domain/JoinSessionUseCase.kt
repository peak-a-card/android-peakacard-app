package com.peakacard.app.session.domain

import com.peakacard.app.session.data.repository.JoinSessionRepository
import com.peakacard.app.session.domain.model.JoinSessionRequest
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class JoinSessionUseCase(private val repository: JoinSessionRepository) {
    suspend fun joinSession(request: JoinSessionRequest):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return repository.joinSession(request)
    }
}