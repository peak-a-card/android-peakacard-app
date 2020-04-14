package com.peakacard.app.voting.data.datasource.remote.model

data class ParticipantsVotationRequest(
    val sessionId: String,
    val votationTitle: String
)
