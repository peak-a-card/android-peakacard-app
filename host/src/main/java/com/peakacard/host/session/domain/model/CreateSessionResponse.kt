package com.peakacard.host.session.domain.model

sealed class CreateSessionResponse {

    data class Success(val id: Int) : CreateSessionResponse()
    object Error : CreateSessionResponse()
}
