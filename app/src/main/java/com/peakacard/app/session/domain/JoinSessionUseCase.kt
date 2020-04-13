package com.peakacard.app.session.domain

import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.app.session.domain.model.UserSession
import com.peakacard.app.user.data.repository.UserRepository
import com.peakacard.core.Either

class JoinSessionUseCase(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {
    suspend fun joinSession(userSession: UserSession):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return sessionRepository.joinSession(userSession)
            .also {
                it.fold(
                    {
                        // DO NOTHING
                    },
                    {
                        sessionRepository.setCurrentSession(userSession.sessionCode)
                        userRepository.saveCurrentUser(userSession.user)
                    })
            }
    }
}
