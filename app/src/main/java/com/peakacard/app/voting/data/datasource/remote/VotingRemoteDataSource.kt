package com.peakacard.app.voting.data.datasource.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.peakacard.app.session.data.model.SessionDataModel
import com.peakacard.app.session.data.model.VotingDataModel
import com.peakacard.app.voting.data.datasource.remote.model.VotingResponse
import com.peakacard.core.Either
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

    suspend fun listenVoting(sessionId: String): Flow<Either<VotingResponse.Error, VotingResponse.Success>> =
        callbackFlow {
            val session = database.collection(SessionDataModel.COLLECTION_ID)
            val votations: CollectionReference =
                session.document(sessionId).collection(SessionDataModel.VOTATIONS)


            val subscription = votations.addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    offer(Either.Left(VotingResponse.Error.RemoteException))
                } else {
                    val document = snapshot?.documentChanges?.firstOrNull()
                    val voting = document?.document
                    val votingStatus =
                        VotingDataModel.Status.fromString(voting?.getString(VotingDataModel.STATUS))
                    when (votingStatus) {
                        VotingDataModel.Status.STARTED -> {
                            val votingTitle = voting?.id
                            if (votingTitle == null) {
                                offer(Either.Left(VotingResponse.Error.NoVotingStarted))
                            } else {
                                offer(Either.Right(VotingResponse.Success(votingTitle)))
                            }
                        }
                        else -> {
                            // DO NOTHING
                        }
                    }
                }
            }

            awaitClose { subscription.remove() }
        }
}
