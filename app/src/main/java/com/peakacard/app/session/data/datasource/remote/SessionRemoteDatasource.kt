package com.peakacard.app.session.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.app.session.data.model.JoinSessionRequestDataModel
import com.peakacard.app.session.data.model.JoinSessionResponseDataModel
import com.peakacard.app.session.data.model.ParticipantDataModel
import com.peakacard.app.session.data.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.tasks.await

class SessionRemoteDatasource(private val database: FirebaseFirestore) {

    suspend fun joinSession(joinSessionRequest: JoinSessionRequestDataModel):
            Either<JoinSessionResponseDataModel.Error, JoinSessionResponseDataModel.Success> {
        return try {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val sessionId = joinSessionRequest.code
            val currentSession = session.document(sessionId).get().await()
            if (currentSession.exists()) {
                val participants =
                    session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)

                with(joinSessionRequest.user) {
                    participants.document(uid).set(
                        mapOf(
                            ParticipantDataModel.PARTICIPANT_NAME to name,
                            ParticipantDataModel.PARTICIPANT_MAIL to email
                        )
                    )
                }
                Either.Right(JoinSessionResponseDataModel.Success)
            } else {
                Either.Left(JoinSessionResponseDataModel.Error.NoSessionFound)
            }
        } catch (e: FirebaseFirestoreException) {
            Either.Left(JoinSessionResponseDataModel.Error.RemoteException)
        }
    }
}
