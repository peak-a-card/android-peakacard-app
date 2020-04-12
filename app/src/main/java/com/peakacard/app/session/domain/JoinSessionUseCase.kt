package com.peakacard.app.session.domain

import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.session.domain.model.JoinSessionRequest
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class JoinSessionUseCase(private val repository: SessionRepository) {
    suspend fun joinSession(request: JoinSessionRequest):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return repository.joinSession(request).also {
            it.fold({}, { repository.setCurrentSession(request.sessionCode) })
        }
    }
}
