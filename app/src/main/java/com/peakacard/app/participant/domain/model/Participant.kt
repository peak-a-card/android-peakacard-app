package com.peakacard.app.participant.domain.model

sealed class Participant(
    open val email: String,
    open val name: String
) {
    class Joined(override val email: String, override val name: String) : Participant(email, name)
    class Left(override val email: String, override val name: String) : Participant(email, name)
}
