package com.peakacard.app.participant.data.datasource.remote

import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantDataModel
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantResponse
import com.peakacard.app.participant.data.datasource.remote.model.ParticipantsResponse
import com.peakacard.app.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ParticipantRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun getAllSessionParticipants(sessionId: String): Either<ParticipantsResponse.Error, ParticipantsResponse.Success> {
        return try {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val participants =
                session.document(sessionId).collection(SessionDataModel.PARTICIPANTS).get().await()
                    .toObjects(ParticipantDataModel::class.java)
            Either.Right(ParticipantsResponse.Success(participants))
        } catch (e: FirebaseFirestoreException) {
            Either.Left(ParticipantsResponse.Error)
        }
    }

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
                        when (documentChange.type) {
                            DocumentChange.Type.ADDED -> offer(
                                Either.Right(
                                    ParticipantResponse.Success.Joined(
                                        participant
                                    )
                                )
                            )
                            DocumentChange.Type.MODIFIED -> {
                                // DO NOTHING
                            }
                            DocumentChange.Type.REMOVED -> {
                                offer(Either.Right(ParticipantResponse.Success.Left(participant)))
                            }
                        }
                    }
                }
            }

            awaitClose { subscription.remove() }
        }
    }
}
