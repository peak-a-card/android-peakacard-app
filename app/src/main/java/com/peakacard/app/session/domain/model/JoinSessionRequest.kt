package com.peakacard.app.session.domain.model

data class JoinSessionRequest(val participantName: ParticipantName, val sessionCode: SessionCode)

inline class ParticipantName(val value: String)
inline class SessionCode(val value: String)