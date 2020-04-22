package com.peakacard.app.session.domain

import com.peakacard.core.Either
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.session.domain.model.JoinSessionResponse
import com.peakacard.session.domain.model.UserSession
import com.peakacard.user.data.repository.UserRepository

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
