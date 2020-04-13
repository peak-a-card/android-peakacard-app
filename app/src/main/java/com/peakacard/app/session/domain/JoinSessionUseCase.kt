package com.peakacard.app.session.domain

import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.session.domain.model.UserSession
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class JoinSessionUseCase(private val repository: SessionRepository) {
    suspend fun joinSession(userSession: UserSession):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return repository.joinSession(userSession).also {
            it.fold({}, { repository.setCurrentSession(userSession.sessionCode) })
        }
    }
}
