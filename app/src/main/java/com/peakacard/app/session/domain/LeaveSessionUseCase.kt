package com.peakacard.app.session.domain

import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.session.domain.model.LeaveSessionResponse
import com.peakacard.session.domain.model.UserSession
import com.peakacard.user.data.repository.UserRepository
import com.peakacard.core.Either

class LeaveSessionUseCase(
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository
) {

    suspend fun leaveSession():
            Either<LeaveSessionResponse.Error, LeaveSessionResponse.Success> {
        val currentUser = userRepository.getCurrentUser()
        return if (currentUser == null) {
            Either.Left(LeaveSessionResponse.Error.NoUserFound)
        } else {
            val sessionId = sessionRepository.getCurrentSession()
            if (sessionId == null) {
                Either.Left(LeaveSessionResponse.Error.NoSessionFound)
            } else {
                val userSession = UserSession(currentUser, sessionId)
                sessionRepository.leaveSession(userSession)
            }
        }.also {
            it.fold(
                {
                    // DO NOTHING
                },
                {
                    sessionRepository.setCurrentSession(null)
                }
            )
        }
    }
}
