package com.peakacard.app.session.data.repository

import com.peakacard.app.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.app.session.data.datasource.remote.SessionRemoteDatasource
import com.peakacard.app.session.data.datasource.remote.model.SessionRequest
import com.peakacard.app.session.data.datasource.remote.model.SessionResponse
import com.peakacard.app.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.app.session.domain.model.UserSession
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class SessionRepository(
    private val remoteDatasource: SessionRemoteDatasource,
    private val localDataSource: SessionLocalDataSource,
    private val userMapper: UserMapper
) {

    suspend fun joinSession(request: UserSession):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return remoteDatasource.joinSession(
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

    fun setCurrentSession(sessionId: String) {
        localDataSource.saveSessionId(sessionId)
    }

    fun getCurrentSession(): String? {
        return localDataSource.getSessionId()
    }
}
