package com.peakacard.app.start.domain

import com.peakacard.app.start.data.repository.StartSessionRepository
import com.peakacard.app.start.domain.model.StartSessionRequest
import com.peakacard.app.start.domain.model.StartSessionResponse
import com.peakacard.core.Either

class StartSessionUseCase(private val repository: StartSessionRepository) {
    suspend fun startSession(request: StartSessionRequest):
            Either<StartSessionResponse.Error, StartSessionResponse.Success> {
        return repository.startSession(request)
    }
}