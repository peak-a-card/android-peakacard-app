package com.peakacard.card.domain.model

sealed class SendVoteResponseError {
  object Unspecified : SendVoteResponseError()
  object SessionNotFound : SendVoteResponseError()
  object VotationNotFound : SendVoteResponseError()
  object UserNotFound : SendVoteResponseError()
}
