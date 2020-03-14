package com.peakacard.app.start.data.model

data class StartSessionRequestDataModel(val id: SessionIdDataModel, val participant: ParticipantDataModel)

inline class SessionIdDataModel(val value: String)
inline class ParticipantDataModel(val value: String)
