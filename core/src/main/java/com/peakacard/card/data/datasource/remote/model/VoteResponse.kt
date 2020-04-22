package com.peakacard.card.data.datasource.remote.model

sealed class VoteResponse {
  object Success : VoteResponse()
  sealed class Error : VoteResponse() {
    object RemoteException : Error()
    object VotationNotFound : Error()
  }
}
