package com.peakacard.app.card.data.datasource.remote.model

data class VoteRequest(
    val sessionId: String,
    val votingTitle: String,
    val uid: String,
    val score: Float
)
