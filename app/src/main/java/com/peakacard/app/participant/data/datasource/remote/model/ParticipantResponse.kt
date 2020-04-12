package com.peakacard.app.participant.data.datasource.remote.model

sealed class ParticipantResponse {
    data class Success(val participant: ParticipantDataModel) : ParticipantResponse()
    object Error : ParticipantResponse()
}
