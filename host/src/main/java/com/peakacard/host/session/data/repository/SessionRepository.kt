package com.peakacard.host.session.data.repository

import com.peakacard.core.Either
import com.peakacard.host.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.host.session.data.datasource.remote.model.SessionIdsResponse
import com.peakacard.host.session.domain.model.CreateSessionResponse
import com.peakacard.host.session.domain.model.GetAllSessionIdsResponse

class SessionRepository(private val sessionRemoteDataSource: SessionRemoteDataSource) {

    suspend fun getAllSessionIds(): Either<GetAllSessionIdsResponse.Error, GetAllSessionIdsResponse.Success> {
        return sessionRemoteDataSource.getAllSessionIds().fold(
            { error ->
                when (error) {
                    SessionIdsResponse.Error.RemoteException -> {
                        Either.Left(GetAllSessionIdsResponse.Error.Unspecified)
                    }
                    SessionIdsResponse.Error.NoSessionIdsFound -> {
                        Either.Left(GetAllSessionIdsResponse.Error.NoSessionStarted)
                    }
                }
            },
            { success -> Either.Right(GetAllSessionIdsResponse.Success(success.ids)) }
        )
    }

    suspend fun createSession(id: Int): Either<CreateSessionResponse.Error, CreateSessionResponse.Success> {
        return sessionRemoteDataSource.createSession(id.toString()).fold(
            { Either.Left(CreateSessionResponse.Error) },
            { Either.Right(CreateSessionResponse.Success(id)) }
        )
    }
}
