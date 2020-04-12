package com.peakacard.app.participant.data.datasource.remote.model

data class ParticipantDataModel(
    val email: String,
    val name: String
) {

    constructor() : this("", "")

    companion object {
        const val PARTICIPANT_NAME = "name"
        const val PARTICIPANT_MAIL = "email"
    }
}
