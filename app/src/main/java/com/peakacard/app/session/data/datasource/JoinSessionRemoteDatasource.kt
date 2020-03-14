package com.peakacard.app.session.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.app.session.data.model.JoinSessionRequestDataModel
import com.peakacard.app.session.data.model.JoinSessionResponseDataModel
import com.peakacard.app.session.data.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.tasks.await

class JoinSessionRemoteDatasource(private val database: FirebaseFirestore) {

    @Suppress("UNCHECKED_CAST")
    suspend fun joinSession(joinSessionRequest: JoinSessionRequestDataModel):
            Either<JoinSessionResponseDataModel.Error, JoinSessionResponseDataModel.Success> {
        return try {
            val sessions = database.collection("sessions")
            val sessionId = joinSessionRequest.id.value
            val dbSession = sessions.document(sessionId).get().await()
            if (dbSession.exists()) {
                val participants = dbSession.get(SessionDataModel.PARTICIPANTS)
                (participants as MutableList<String>).add(joinSessionRequest.participant.value)
                sessions.document(sessionId).update(SessionDataModel.PARTICIPANTS, participants)
                Either.Right(JoinSessionResponseDataModel.Success)
            } else {
                Either.Left(JoinSessionResponseDataModel.Error.NoSessionFound)
            }
        } catch (e: FirebaseFirestoreException) {
            Either.Left(JoinSessionResponseDataModel.Error.RemoteException)
        }
    }
}