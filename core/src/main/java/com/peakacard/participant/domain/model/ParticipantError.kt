package com.peakacard.participant.domain.model

sealed class ParticipantError {
    object NoParticipants : ParticipantError()
    object Unspecified : ParticipantError()
}
