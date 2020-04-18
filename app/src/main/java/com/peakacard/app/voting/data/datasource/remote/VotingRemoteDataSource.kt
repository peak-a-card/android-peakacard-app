package com.peakacard.app.voting.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.app.voting.data.datasource.remote.model.*
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.voting.data.datasource.remote.model.VotingDataModel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun listenStartedVoting(sessionId: String): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return listenVotingStatus(sessionId, VotingDataModel.Status.STARTED)
    }

    suspend fun listenEndedVoting(
        sessionId: String,
        votationTitle: String
    ): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return listenVotingStatus(sessionId, VotingDataModel.Status.ENDED).filter {
            it.fold(
                { return@fold true },
                { votingSuccess -> return@fold votingSuccess.votingTitle == votationTitle }
            )
        }
    }

    private suspend fun listenVotingStatus(
        sessionId: String,
        status: VotingDataModel.Status
    ): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
        return callbackFlow {
            val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
            val votations = session.document(sessionId).collection(SessionDataModel.VOTATIONS)
                .orderBy(VotingDataModel.CREATION_DATE)

            val subscription = votations.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(VotingStatusResponse.Error.RemoteException))
                } else {
                    val votingDocuments = snapshot?.documents
                    val votingsByStatus = votingDocuments?.map {
                        it.toObject(VotingDataModel::class.java)
                    }?.filter {
                        VotingDataModel.Status.fromString(it?.status) == status
                    }

                    if (votingsByStatus.isNullOrEmpty()) {
                        when (status) {
                            VotingDataModel.Status.STARTED -> {
                                offer(Either.Left(VotingStatusResponse.Error.NoVotingStarted))
                            }
                            VotingDataModel.Status.ENDED -> {
                                offer(Either.Left(VotingStatusResponse.Error.NoVotingEnded))
                            }
                        }
                    } else {
                        votingsByStatus.forEach { votingDataModel ->
                            if (votingDataModel != null) {
                                offer(Either.Right(VotingStatusResponse.Success(votingDataModel.name)))
                            }
                        }
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
            val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
            val votation =
                session.document(participantsVotationRequest.sessionId)
                    .collection(SessionDataModel.VOTATIONS)
                    .document(participantsVotationRequest.votationTitle)

            val subscription = votation.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(ParticipantsVotationResponse.Error.RemoteException))
                } else {
                    val participantsVotation =
                        snapshot?.get(VotingDataModel.PARTICIPANT_VOTATION) as Map<String, Float>

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
