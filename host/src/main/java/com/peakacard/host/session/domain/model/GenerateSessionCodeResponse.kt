package com.peakacard.host.session.domain.model

sealed class GenerateSessionCodeResponse {

    data class Success(val id: Int) : GenerateSessionCodeResponse()
    object Error : GenerateSessionCodeResponse()
}
