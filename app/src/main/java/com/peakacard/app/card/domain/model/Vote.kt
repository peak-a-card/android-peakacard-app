package com.peakacard.app.card.domain.model

data class Vote(
    val sessionId: String,
    val votingTitle: String,
    val uid: String,
    val score: Float
)
