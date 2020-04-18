package com.peakacard.host.voting.data.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.host.voting.data.datasource.remote.model.VotingResponse
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.voting.data.datasource.remote.model.VotingDataModel
import kotlinx.coroutines.tasks.await

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun createVoting(
        sessionId: String,
        title: String
    ): Either<VotingResponse.RemoteError, VotingResponse.Success> {
        val votationValues = mapOf(
            VotingDataModel.CREATION_DATE to Timestamp.now(),
            VotingDataModel.NAME to title,
            VotingDataModel.STATUS to VotingDataModel.Status.STARTED.toString()
        )

        return try {
            val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
            session.document(sessionId).collection(SessionDataModel.VOTATIONS)
                .document(title)
                .set(votationValues)
                .await()
            Either.Right(VotingResponse.Success)
        } catch (e: FirebaseFirestoreException) {
            Either.Left(VotingResponse.RemoteError)
        }
    }
}
