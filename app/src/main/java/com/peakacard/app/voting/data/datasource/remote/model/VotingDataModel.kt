package com.peakacard.app.voting.data.datasource.remote.model


data class VotingDataModel(val name: String, val status: String) {

    constructor() : this("", "")

    companion object {
        const val STATUS = "status"
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
