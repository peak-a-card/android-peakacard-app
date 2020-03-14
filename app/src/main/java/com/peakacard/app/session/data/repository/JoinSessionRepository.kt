package com.peakacard.app.session.data.repository

import com.peakacard.app.session.data.datasource.JoinSessionRemoteDatasource
import com.peakacard.app.session.data.model.ParticipantDataModel
import com.peakacard.app.session.data.model.SessionIdDataModel
import com.peakacard.app.session.data.model.JoinSessionRequestDataModel
import com.peakacard.app.session.data.model.JoinSessionResponseDataModel
import com.peakacard.app.session.domain.model.JoinSessionRequest
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.core.Either

class JoinSessionRepository(private val remoteDatasource: JoinSessionRemoteDatasource) {
    suspend fun joinSession(request: JoinSessionRequest):
            Either<JoinSessionResponse.Error, JoinSessionResponse.Success> {
        return remoteDatasource.joinSession(
            JoinSessionRequestDataModel(
                SessionIdDataModel(request.sessionCode.value),
                ParticipantDataModel(request.participantName.value)
            )
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