package com.peakacard.session.data.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.participant.data.datasource.remote.model.ParticipantDataModel
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.session.data.datasource.remote.model.SessionIdsResponse
import com.peakacard.session.data.datasource.remote.model.SessionRequest
import com.peakacard.session.data.datasource.remote.model.SessionResponse
import kotlinx.coroutines.tasks.await

class SessionRemoteDataSource(private val database: FirebaseFirestore) {

  suspend fun joinSession(sessionRequest: SessionRequest):
    Either<SessionResponse.Error, SessionResponse.Success> {
    return try {
      val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
      val sessionId = sessionRequest.code
      val currentSession = session.document(sessionId).get().await()
      if (currentSession.exists()) {
        val participants =
          session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)

        with(sessionRequest.user) {
          participants.document(uid).set(
            mapOf(
              ParticipantDataModel.PARTICIPANT_ID to uid,
              ParticipantDataModel.PARTICIPANT_NAME to name,
              ParticipantDataModel.PARTICIPANT_MAIL to email
            )
          )
        }
        Either.Right(SessionResponse.Success)
      } else {
        Either.Left(SessionResponse.Error.NoSessionFound)
      }
    } catch (e: FirebaseFirestoreException) {
      Either.Left(SessionResponse.Error.RemoteException)
    }
  }

  suspend fun leaveSession(sessionRequest: SessionRequest):
    Either<SessionResponse.Error, SessionResponse.Success> {
    return try {
      val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
      val sessionId = sessionRequest.code
      val currentSession = session.document(sessionId).get().await()
      if (currentSession.exists()) {
        session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)
          .document(sessionRequest.user.uid).delete().await()
        Either.Right(SessionResponse.Success)
      } else {
        Either.Left(SessionResponse.Error.NoSessionFound)
      }
    } catch (e: FirebaseFirestoreException) {
      Either.Left(SessionResponse.Error.RemoteException)
    }
  }

  suspend fun getAllSessionIds(): Either<SessionIdsResponse.Error, SessionIdsResponse.Success> {
    return try {
      val sessionIds = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
        .get().await()
        .documents
        .map { it.id }

      if (sessionIds.isNullOrEmpty()) {
        Either.Left(SessionIdsResponse.Error.NoSessionIdsFound)
      } else {
        Either.Right(SessionIdsResponse.Success(sessionIds))
      }
    } catch (e: FirebaseFirestoreException) {
      Either.Left(SessionIdsResponse.Error.RemoteException)
    }
  }

  suspend fun createSession(id: String): Either<SessionResponse.Error, SessionResponse.Success> {
    return try {
      database.collection(PeakDataModel.ROOT_COLLECTION_ID)
        .document(id)
        .set(mapOf(SessionDataModel.CREATION_DATE to Timestamp.now()))
        .await()
      Either.Right(SessionResponse.Success)
    } catch (e: FirebaseFirestoreException) {
      Either.Left(SessionResponse.Error.RemoteException)
    }
  }
}
