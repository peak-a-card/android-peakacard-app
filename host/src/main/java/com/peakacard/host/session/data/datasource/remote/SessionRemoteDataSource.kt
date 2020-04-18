package com.peakacard.host.session.data.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.host.session.data.datasource.remote.model.SessionIdsResponse
import com.peakacard.host.session.data.datasource.remote.model.SessionResponse
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import kotlinx.coroutines.tasks.await

class SessionRemoteDataSource(private val database: FirebaseFirestore) {

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

    suspend fun createSession(id: String): Either<SessionResponse.RemoteError, SessionResponse.Success> {
        return try {
            database.collection(PeakDataModel.ROOT_COLLECTION_ID)
                .document(id)
                .set(mapOf(SessionDataModel.CREATION_DATE to Timestamp.now()))
                .await()
            Either.Right(SessionResponse.Success)
        } catch (e: FirebaseFirestoreException) {
            Either.Left(SessionResponse.RemoteError)
        }
    }
}
