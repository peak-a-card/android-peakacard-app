package com.peakacard.app.start.domain.model

data class StartSessionRequest(val participantName: ParticipantName, val sessionCode: SessionCode)

inline class ParticipantName(val value: String)
inline class SessionCode(val value: String)