package com.peakacard.session.data.repository

import com.peakacard.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.session.data.datasource.remote.model.SessionRequest
import com.peakacard.session.data.datasource.remote.model.SessionResponse
import com.peakacard.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.session.domain.model.JoinSessionResponse
import com.peakacard.session.domain.model.LeaveSessionResponse
import com.peakacard.session.domain.model.UserSession
import com.peakacard.core.Either

class SessionRepository(
    private val remoteDataSource: SessionRemoteDataSource,
    private val localDataSource: SessionLocalDataSource,
    private val userMapper: UserMapper
) {

    suspend fun joinSession(request: UserSession):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return remoteDataSource.joinSession(
            SessionRequest(userMapper.map(request.user), request.sessionCode)
        ).fold(
            {
                when (it) {
                    SessionResponse.Error.NoSessionFound -> {
                        Either.Left(JoinSessionResponse.Error.NoSessionFound)
                    }
                    SessionResponse.Error.RemoteException -> {
                        Either.Left(JoinSessionResponse.Error.Unspecified)
                    }
                }
            },
            {
                Either.Right(JoinSessionResponse.Success)
            })
    }

    fun setCurrentSession(sessionId: String?) {
        localDataSource.saveSessionId(sessionId)
    }

    fun getCurrentSession(): String? {
        return localDataSource.getSessionId()
    }

    suspend fun leaveSession(request: UserSession):
            Either<LeaveSessionResponse.Error, LeaveSessionResponse.Success> {
        return remoteDataSource.leaveSession(
            SessionRequest(
                userMapper.map(request.user),
                request.sessionCode
            )
        ).fold(
            {
                when (it) {
                    SessionResponse.Error.NoSessionFound -> {
                        Either.Left(LeaveSessionResponse.Error.NoSessionFound)
                    }
                    SessionResponse.Error.RemoteException -> {
                        Either.Left(LeaveSessionResponse.Error.Unspecified)
                    }
                }
            },
            {
                Either.Right(LeaveSessionResponse.Success)
            })
    }
}
