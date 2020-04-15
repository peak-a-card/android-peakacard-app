package com.peakacard.app.voting.data.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.app.card.data.datasource.remote.model.VotationDataModel
import com.peakacard.app.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantVotationDataModel
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantsVotationRequest
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantsVotationResponse
import com.peakacard.app.voting.data.datasource.remote.model.VotingDataModel
import com.peakacard.app.voting.data.datasource.remote.model.VotingResponse
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun listenStartedVoting(sessionId: String): Flow<Either<VotingResponse.Error, VotingResponse.Success>> {
        return callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val votations: CollectionReference =
                session.document(sessionId).collection(SessionDataModel.VOTATIONS)

            val subscription = votations.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(VotingResponse.Error.RemoteException))
                } else {
                    val votingDocuments = snapshot?.documents
                    val startedVoting = votingDocuments?.map {
                        it.toObject(VotingDataModel::class.java)
                    }?.firstOrNull {
                        VotingDataModel.Status.fromString(it?.status) == VotingDataModel.Status.STARTED
                    }

                    if (startedVoting == null) {
                        offer(Either.Left(VotingResponse.Error.NoVotingStarted))
                    } else {
                        offer(Either.Right(VotingResponse.Success(startedVoting.name)))
                    }
                }
            }

            awaitClose { subscription.remove() }
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun listenForParticipantsVotation(participantsVotationRequest: ParticipantsVotationRequest):
            Flow<Either<ParticipantsVotationResponse.Error, ParticipantsVotationResponse.Success>> {
        return callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val votation =
                session.document(participantsVotationRequest.sessionId)
                    .collection(SessionDataModel.VOTATIONS)
                    .document(participantsVotationRequest.votationTitle)

            val subscription = votation.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(ParticipantsVotationResponse.Error.RemoteException))
                } else {
                    val participantsVotation =
                        snapshot?.get(VotationDataModel.PARTICIPANT_VOTATION) as Map<String, Float>

                    val participantVotationDataModels = participantsVotation.map { (key, value) ->
                        ParticipantVotationDataModel(
                            key,
                            value
                        )
                    }

                    offer(
                        Either.Right(
                            ParticipantsVotationResponse.Success(
                                participantVotationDataModels
                            )
                        )
                    )
                }
            }

            awaitClose { subscription.remove() }
        }
    }
}
