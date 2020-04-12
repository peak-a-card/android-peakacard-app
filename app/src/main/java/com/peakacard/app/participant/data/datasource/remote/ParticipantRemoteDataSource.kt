package com.peakacard.app.participant.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantDataModel
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantResponse
import com.peakacard.app.session.data.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ParticipantRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun getSessionParticipant(sessionId: String): Flow<Either<ParticipantResponse.Error, ParticipantResponse.Success>> {
        return callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val participants = session.document(sessionId).collection(SessionDataModel.PARTICIPANTS)

            val subscription = participants.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(ParticipantResponse.Error))
                } else {
                    val documentChange = snapshot?.documentChanges?.firstOrNull()
                    val participant =
                        documentChange?.document?.toObject(ParticipantDataModel::class.java)
                    if (participant == null) {
                        offer(Either.Left(ParticipantResponse.Error))
                    } else {
                        offer(Either.Right(ParticipantResponse.Success(participant)))
                    }
                }
            }

            awaitClose { subscription.remove() }
        }
    }
}
