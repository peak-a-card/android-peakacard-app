package com.peakacard.app.participant.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantDataModel
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantsResponse
import com.peakacard.app.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ParticipantRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun getSessionParticipants(sessionId: String): Flow<Either<ParticipantsResponse.Error, ParticipantsResponse.Success>> {
        return callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val participants = session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)

            val subscription = participants.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(ParticipantsResponse.Error))
                } else {
                    val participantDocuments = snapshot?.documents
                    val participantDataModels = participantDocuments?.mapNotNull { document ->
                        document.toObject(ParticipantDataModel::class.java)
                    }
                    if (participantDataModels.isNullOrEmpty()) {
                        offer(Either.Left(ParticipantsResponse.Error))
                    } else {
                        offer(Either.Right(ParticipantsResponse.Success(participantDataModels)))
                    }
                }
            }

            awaitClose { subscription.remove() }
        }
    }
}
