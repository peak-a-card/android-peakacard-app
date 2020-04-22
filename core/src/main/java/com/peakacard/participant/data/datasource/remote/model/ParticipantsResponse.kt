package com.peakacard.participant.data.datasource.remote.model

sealed class ParticipantsResponse {
  data class Success(val participants: List<ParticipantDataModel>) : ParticipantsResponse()
  sealed class Error : ParticipantsResponse() {
    object NoParticipants : Error()
    object RemoteException : Error()
  }
}
