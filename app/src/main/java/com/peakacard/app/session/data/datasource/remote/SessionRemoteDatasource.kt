package com.peakacard.app.session.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.app.session.data.datasource.remote.model.SessionRequest
import com.peakacard.app.session.data.datasource.remote.model.SessionResponse
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantDataModel
import com.peakacard.app.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.tasks.await

class SessionRemoteDatasource(private val database: FirebaseFirestore) {

    suspend fun joinSession(sessionRequest: SessionRequest):
            Either<SessionResponse.Error, SessionResponse.Success> {
        return try {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val sessionId = sessionRequest.code
            val currentSession = session.document(sessionId).get().await()
            if (currentSession.exists()) {
                val participants =
                    session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)

                with(sessionRequest.user) {
                    participants.document(uid).set(
                        mapOf(
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
}
