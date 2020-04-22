package com.peakacard.participant.data.datasource.remote.model

data class ParticipantDataModel(
  val id: String,
  val email: String,
  val name: String
) {

  constructor() : this("", "", "")

  companion object {
    const val PARTICIPANT_ID = "id"
    const val PARTICIPANT_NAME = "name"
    const val PARTICIPANT_MAIL = "email"
  }
}
