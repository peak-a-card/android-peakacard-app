package com.peakacard.app.session.data.repository

import com.peakacard.app.session.data.datasource.JoinSessionRemoteDatasource
import com.peakacard.app.session.data.model.JoinSessionRequestDataModel
import com.peakacard.app.session.data.model.JoinSessionResponseDataModel
import com.peakacard.app.session.data.model.mapper.UserMapper
import com.peakacard.app.session.domain.model.JoinSessionRequest
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class JoinSessionRepository(
    private val remoteDatasource: JoinSessionRemoteDatasource,
    private val userMapper: UserMapper
) {

    suspend fun joinSession(request: JoinSessionRequest):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return remoteDatasource.joinSession(
            JoinSessionRequestDataModel(userMapper.map(request.user), request.sessionCode)
        ).fold(
            {
                when (it) {
                    JoinSessionResponseDataModel.Error.NoSessionFound -> {
                        Either.Left(JoinSessionResponse.Error.NoSessionFound)
                    }
                    JoinSessionResponseDataModel.Error.RemoteException -> {
                        Either.Left(JoinSessionResponse.Error.Unspecified)
                    }
                }
            },
            {
                Either.Right(JoinSessionResponse.Success)
            })
    }
}
