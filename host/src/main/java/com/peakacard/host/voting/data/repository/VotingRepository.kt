package com.peakacard.host.voting.data.repository

import com.peakacard.core.Either
import com.peakacard.host.voting.data.datasource.remote.VotingRemoteDataSource
import com.peakacard.host.voting.domain.model.CreateVotingResponse

class VotingRepository(private val votingRemoteDataSource: VotingRemoteDataSource) {

    suspend fun createVoting(
        sessionId: String,
        title: String
    ): Either<CreateVotingResponse.Error, CreateVotingResponse.Success> {
        return votingRemoteDataSource.createVoting(sessionId, title).fold(
            { Either.Left(CreateVotingResponse.Error.Unspecified) },
            { Either.Right(CreateVotingResponse.Success(title)) }
        )
    }
}
