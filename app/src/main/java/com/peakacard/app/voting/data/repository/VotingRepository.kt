package com.peakacard.app.voting.data.repository

import com.peakacard.app.voting.data.datasource.local.VotingLocalDataSource
import com.peakacard.app.voting.data.datasource.remote.VotingRemoteDataSource
import com.peakacard.app.voting.data.datasource.remote.model.VotingResponse
import com.peakacard.app.voting.domain.model.GetVotingError
import com.peakacard.app.voting.domain.model.Voting
import com.peakacard.core.Either
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VotingRepository(
    private val votingRemoteDataSource: VotingRemoteDataSource,
    private val votingLocalDataSource: VotingLocalDataSource
) {

    suspend fun getVotation(sessionId: String): Flow<Either<GetVotingError, Voting>> {
        return votingRemoteDataSource.listenVoting(sessionId).map { votingResponse ->
            votingResponse.fold(
                {
                    when (it) {
                        VotingResponse.Error.NoVotingStarted -> Either.Left(GetVotingError.NoVotingStarted)
                        VotingResponse.Error.RemoteException -> Either.Left(GetVotingError.Unspecified)
                    }
                },
                {
                    Either.Right(Voting(it.votingTitle))
                }
            )
        }
    }

    fun saveCurrentVoting(voting: Voting) {
        votingLocalDataSource.saveVoting(voting.title)
    }

    fun getCurrentVoting(): Voting? {
        val votingTitle = votingLocalDataSource.getVoting()
        return votingTitle?.let { Voting(it) }
    }
}
