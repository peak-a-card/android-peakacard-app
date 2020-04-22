package com.peakacard.voting.domain.model

sealed class EndVoteResponse {
  data class Success(val title: String) : EndVoteResponse()
  sealed class Error : EndVoteResponse() {
    object NoSessionId : Error()
    object NoVoteRunning : Error()
    object Unspecified : Error()
  }
}
