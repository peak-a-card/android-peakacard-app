package com.peakacard.host.session.domain.model

sealed class CreateSessionResponse {

    data class Success(val id: String) : CreateSessionResponse()
    object Error : CreateSessionResponse()
}
