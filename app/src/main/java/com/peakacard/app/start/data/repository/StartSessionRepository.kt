package com.peakacard.app.start.data.repository

import com.peakacard.app.start.data.datasource.StartSessionRemoteDatasource
import com.peakacard.app.start.data.model.ParticipantDataModel
import com.peakacard.app.start.data.model.SessionIdDataModel
import com.peakacard.app.start.data.model.StartSessionRequestDataModel
import com.peakacard.app.start.data.model.StartSessionResponseDataModel
import com.peakacard.app.start.domain.model.StartSessionRequest
import com.peakacard.app.start.domain.model.StartSessionResponse
import com.peakacard.core.Either

class StartSessionRepository(private val remoteDatasource: StartSessionRemoteDatasource) {
    suspend fun startSession(request: StartSessionRequest):
            Either<StartSessionResponse.Error, StartSessionResponse.Success> {
        return remoteDatasource.startSession(
            StartSessionRequestDataModel(
                SessionIdDataModel(request.sessionCode.value),
                ParticipantDataModel(request.participantName.value)
            )
        ).fold(
            {
                when (it) {
                    StartSessionResponseDataModel.Error.NoSessionFound -> {
                        Either.Left(StartSessionResponse.Error.NoSessionFound)
                    }
                    StartSessionResponseDataModel.Error.RemoteException -> {
                        Either.Left(StartSessionResponse.Error.Unspecified)
                    }
                }
            },
            {
                Either.Right(StartSessionResponse.Success)
            })
    }
}