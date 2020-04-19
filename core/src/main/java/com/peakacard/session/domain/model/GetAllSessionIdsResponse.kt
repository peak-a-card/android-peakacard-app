package com.peakacard.session.domain.model

sealed class GetAllSessionIdsResponse {

    class Success(val ids: List<String>) : GetAllSessionIdsResponse()
    sealed class Error : GetAllSessionIdsResponse() {
        object Unspecified : Error()
        object NoSessionStarted : Error()
    }
}
