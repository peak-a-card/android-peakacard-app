package com.peakacard.app.session.data.model

data class JoinSessionRequestDataModel(val id: SessionIdDataModel, val participant: ParticipantDataModel)

inline class SessionIdDataModel(val value: String)
inline class ParticipantDataModel(val value: String)
