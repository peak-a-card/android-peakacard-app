package com.peakacard.voting.data.repository

import com.peakacard.core.Either
import com.peakacard.result.domain.model.GetParticipantsVotationError
import com.peakacard.result.domain.model.GetParticipantsVotationResponse
import com.peakacard.voting.data.datasource.local.VotingLocalDataSource
import com.peakacard.voting.data.datasource.remote.VotingRemoteDataSource
import com.peakacard.voting.data.datasource.remote.model.ParticipantsVotationRequest
import com.peakacard.voting.data.datasource.remote.model.ParticipantsVotationResponse
import com.peakacard.voting.data.datasource.remote.model.VotingStatusResponse
import com.peakacard.voting.domain.model.CreateVotingResponse
import com.peakacard.voting.domain.model.EndVoteResponse
import com.peakacard.voting.domain.model.GetVotingError
import com.peakacard.voting.domain.model.ParticipantsVotation
import com.peakacard.voting.domain.model.Voting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VotingRepository(
  private val votingRemoteDataSource: VotingRemoteDataSource,
  private val votingLocalDataSource: VotingLocalDataSource
) {

  suspend fun getStartedVotation(sessionId: String): Flow<Either<GetVotingError, Voting>> {
    return votingRemoteDataSource.listenStartedVoting(sessionId).map { votingResponse ->
      votingResponse.fold(
        {
          when (it) {
            VotingStatusResponse.Error.NoVotingStarted -> {
              Either.Left(GetVotingError.NoVotingStarted)
            }
            else -> Either.Left(GetVotingError.Unspecified)
          }
        },
        {
          Either.Right(Voting(it.votingTitle))
        }
      )
    }
  }

  suspend fun getEndedVotation(
    sessionId: String,
    votationTitle: String
  ): Flow<Either<GetVotingError, Voting>> {
    return votingRemoteDataSource.listenEndedVoting(sessionId, votationTitle)
      .map { votingResponse ->
        votingResponse.fold(
          {
            when (it) {
              VotingStatusResponse.Error.NoVotingEnded -> {
                Either.Left(GetVotingError.NoVotingEnded)
              }
              else -> Either.Left(GetVotingError.Unspecified)
            }
          },
          {
            Either.Right(Voting(it.votingTitle))
          }
        )
      }
  }

  suspend fun getParticipantsVotation(participantsVotation: ParticipantsVotation):
    Flow<Either<GetParticipantsVotationError, List<GetParticipantsVotationResponse>>> {
    return with(participantsVotation) {
      votingRemoteDataSource.listenForParticipantsVotation(
        ParticipantsVotationRequest(
          sessionId,
          votationTitle
        )
      ).map { votingResponse ->
        votingResponse.fold(
          {
            when (it) {
              ParticipantsVotationResponse.Error.RemoteException -> Either.Left(
                GetParticipantsVotationError.Unspecified
              )
            }
          },
          { participantsVotationResponse ->
            Either.Right(participantsVotationResponse.participantVotationDataModels
              .map {
                GetParticipantsVotationResponse(it.id, it.score)
              })
          }
        )
      }
    }
  }

  suspend fun createVoting(
    sessionId: String,
    title: String
  ): Either<CreateVotingResponse.Error, CreateVotingResponse.Success> {
    return votingRemoteDataSource.createVoting(sessionId, title).fold(
      { Either.Left(CreateVotingResponse.Error.Unspecified) },
      { Either.Right(CreateVotingResponse.Success(title)) }
    )
  }

  suspend fun endVote(
    sessionId: String,
    title: String
  ): Either<EndVoteResponse.Error, EndVoteResponse.Success> {
    return votingRemoteDataSource.endVoting(sessionId, title).fold(
      { Either.Left(EndVoteResponse.Error.Unspecified) },
      { Either.Right(EndVoteResponse.Success(title)) }
    )
  }

  fun saveCurrentVoting(voting: Voting?) {
    votingLocalDataSource.saveVoting(voting?.title)
  }

  fun getCurrentVoting(): Voting? {
    val votingTitle = votingLocalDataSource.getVoting()
    return votingTitle?.let { Voting(it) }
  }
}
