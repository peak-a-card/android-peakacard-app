package com.peakacard.app.participant.data.datasource.remote.model

sealed class ParticipantsResponse {
    data class Success(val participants: List<ParticipantDataModel>) : ParticipantsResponse()
    object Error : ParticipantsResponse()
}
