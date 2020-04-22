package com.peakacard.voting.domain.model

sealed class CreateVotingResponse {
  data class Success(val title: String) : CreateVotingResponse()
  sealed class Error : CreateVotingResponse() {
    object NoSessionId : Error()
    object Unspecified : Error()
  }
}
