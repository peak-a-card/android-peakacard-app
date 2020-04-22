package com.peakacard.session.domain.model

sealed class CreateSessionResponse {

  data class Success(val id: String) : CreateSessionResponse()
  object Error : CreateSessionResponse()
}
