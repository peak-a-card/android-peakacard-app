package com.peakacard.app.start.data.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.app.start.data.model.SessionDataModel
import com.peakacard.app.start.data.model.StartSessionRequestDataModel
import com.peakacard.app.start.data.model.StartSessionResponseDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.tasks.await

class StartSessionRemoteDatasource(private val database: FirebaseFirestore) {

    @Suppress("UNCHECKED_CAST")
    suspend fun startSession(startSessionRequest: StartSessionRequestDataModel):
            Either<StartSessionResponseDataModel.Error, StartSessionResponseDataModel.Success> {
        return try {
            val sessions = database.collection("sessions")
            val sessionId = startSessionRequest.id.value
            val dbSession = sessions.document(sessionId).get().await()
            if (dbSession.exists()) {
                val participants = dbSession.get(SessionDataModel.PARTICIPANTS)
                (participants as MutableList<String>).add(startSessionRequest.participant.value)
                sessions.document(sessionId).update(SessionDataModel.PARTICIPANTS, participants)
                Either.Right(StartSessionResponseDataModel.Success)
            } else {
                Either.Left(StartSessionResponseDataModel.Error.NoSessionFound)
            }
        } catch (e: FirebaseFirestoreException) {
            Either.Left(StartSessionResponseDataModel.Error.RemoteException)
        }
    }
}