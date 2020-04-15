package com.peakacard.app.voting.data.datasource.remote.model

sealed class ParticipantsVotationResponse {
    data class Success(val participantVotationDataModels: List<ParticipantVotationDataModel>) :
        ParticipantsVotationResponse()

    sealed class Error : ParticipantsVotationResponse() {
        object RemoteException : Error()
    }
}
