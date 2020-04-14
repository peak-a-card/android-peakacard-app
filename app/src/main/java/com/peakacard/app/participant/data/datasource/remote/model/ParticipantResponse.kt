package com.peakacard.app.participant.data.datasource.remote.model

sealed class ParticipantResponse {
    class Success(val participants: List<ParticipantDataModel>) : ParticipantResponse()
    object Error : ParticipantResponse()
}
