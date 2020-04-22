package com.peakacard.session.domain.model

sealed class JoinSessionResponse {
  object Success : JoinSessionResponse()

  sealed class Error : JoinSessionResponse() {
    object NoSessionFound : Error()
    object Unspecified : Error()
  }
}
