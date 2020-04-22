package com.peakacard.session.data.datasource.remote.model

sealed class SessionIdsResponse {

  data class Success(val ids: List<String>) : SessionIdsResponse()
  sealed class Error : SessionIdsResponse() {
    object RemoteException : Error()
    object NoSessionIdsFound : Error()
  }
}
