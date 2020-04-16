package com.peakacard.app.voting.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.app.card.data.datasource.remote.model.VotationDataModel
import com.peakacard.app.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantVotationDataModel
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantsVotationRequest
import com.peakacard.app.voting.data.datasource.remote.model.ParticipantsVotationResponse
import com.peakacard.app.voting.data.datasource.remote.model.VotingDataModel
import com.peakacard.app.voting.data.datasource.remote.model.VotingStatusResponse
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun listenStartedVoting(sessionId: String): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return listenVotingStatus(sessionId, VotingDataModel.Status.STARTED)
    }

    suspend fun listenEndedVoting(sessionId: String): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return listenVotingStatus(sessionId, VotingDataModel.Status.ENDED)
    }

    private suspend fun listenVotingStatus(
        sessionId: String,
        status: VotingDataModel.Status
    ): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val votations = session.document(sessionId).collection(SessionDataModel.VOTATIONS)
                .orderBy(VotationDataModel.CREATION_DATE)

            val subscription = votations.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(VotingStatusResponse.Error.RemoteException))
                } else {
                    val votingDocuments = snapshot?.documents
                    val voting = votingDocuments?.map {
                        it.toObject(VotingDataModel::class.java)
                    }?.firstOrNull {
                        VotingDataModel.Status.fromString(it?.status) == status
                    }

                    if (voting == null) {
                        when (status) {
                            VotingDataModel.Status.STARTED -> {
                                offer(Either.Left(VotingStatusResponse.Error.NoVotingStarted))
                            }
                            VotingDataModel.Status.ENDED -> {
                                offer(Either.Left(VotingStatusResponse.Error.NoVotingEnded))
                            }
                        }
                    } else {
                        offer(Either.Right(VotingStatusResponse.Success(voting.name)))
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
