package com.peakacard.app.participant.data.datasource.remote.model

sealed class ParticipantResponse {
    sealed class Success : ParticipantResponse() {
        data class Joined(val participant: ParticipantDataModel) : Success()
        data class Left(val participant: ParticipantDataModel) : Success()
    }

    object Error : ParticipantResponse()
}
