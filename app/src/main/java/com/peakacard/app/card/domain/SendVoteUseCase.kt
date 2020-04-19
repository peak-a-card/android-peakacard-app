package com.peakacard.app.card.domain

import com.peakacard.card.data.repository.CardRepository
import com.peakacard.card.domain.model.Card
import com.peakacard.card.domain.model.SendVoteResponseError
import com.peakacard.card.domain.model.SendVoteSuccess
import com.peakacard.card.domain.model.Vote
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.user.data.repository.UserRepository
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.core.Either

class SendVoteUseCase(
    private val cardRepository: CardRepository,
    private val sessionRepository: SessionRepository,
    private val votingRepository: VotingRepository,
    private val userRepository: UserRepository
) {

    suspend fun sendVote(card: Card): Either<SendVoteResponseError, SendVoteSuccess> {
        val sessionId = sessionRepository.getCurrentSession()
        return if (sessionId == null) {
            return Either.Left(SendVoteResponseError.SessionNotFound)
        } else {
            val voting = votingRepository.getCurrentVoting()
            if (voting == null) {
                Either.Left(SendVoteResponseError.VotationNotFound)
            } else {
                val currentUser = userRepository.getCurrentUser()
                if (currentUser == null) {
                    Either.Left(SendVoteResponseError.UserNotFound)
                } else {
                    val vote = Vote(sessionId, voting.title, currentUser.uid, card.score)
                    cardRepository.sendVote(vote)
                }
            }
        }
    }
}
