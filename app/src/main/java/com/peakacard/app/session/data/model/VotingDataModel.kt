package com.peakacard.app.session.data.model

object VotingDataModel {
    const val STATUS = "status"

    enum class Status {
        STARTED, ENDED;

        companion object {
            fun fromString(status: String?): Status? {
                return values().firstOrNull { it.name == status }
            }
        }
    }
}
