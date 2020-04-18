package com.peakacard.voting.data.datasource.remote.model


data class VotingDataModel(val name: String, val status: String) {

    constructor() : this("", "")

    companion object {
        const val CREATION_DATE = "creationDate"
        const val NAME = "name"
        const val STATUS = "status"
        const val PARTICIPANT_VOTATION = "participant_votation"
    }

    enum class Status {
        STARTED, ENDED;

        companion object {
            fun fromString(status: String?): Status? {
                return values().firstOrNull { it.name == status }
            }
        }
    }
}
