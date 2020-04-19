package com.peakacard.card.data.repository

import com.peakacard.card.data.datasource.remote.CardRemoteDataSource
import com.peakacard.card.data.datasource.remote.model.VoteResponse
import com.peakacard.card.data.datasource.remote.model.mapper.VoteMapper
import com.peakacard.card.domain.model.SendVoteResponseError
import com.peakacard.card.domain.model.SendVoteSuccess
import com.peakacard.card.domain.model.Vote
import com.peakacard.core.Either

class CardRepository(
    private val cardRemoteDataSource: CardRemoteDataSource,
    private val voteMapper: VoteMapper
) {

    suspend fun sendVote(vote: Vote): Either<SendVoteResponseError, SendVoteSuccess> {
        return cardRemoteDataSource.sendVote(voteMapper.map(vote)).fold(
            { error ->
                Either.Left(
                    when (error) {
                        VoteResponse.Error.RemoteException -> SendVoteResponseError.Unspecified
                        VoteResponse.Error.VotationNotFound -> SendVoteResponseError.VotationNotFound
                    }
                )
            },
            { Either.Right(SendVoteSuccess) }
        )
    }
}
