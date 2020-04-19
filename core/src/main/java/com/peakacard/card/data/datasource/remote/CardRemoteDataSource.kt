package com.peakacard.card.data.datasource.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import com.peakacard.card.data.datasource.remote.model.VoteRequest
import com.peakacard.card.data.datasource.remote.model.VoteResponse
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.voting.data.datasource.remote.model.VotingDataModel
import kotlinx.coroutines.tasks.await

class CardRemoteDataSource(private val database: FirebaseFirestore) {

    @Suppress("UNCHECKED_CAST")
    suspend fun sendVote(voteRequest: VoteRequest): Either<VoteResponse.Error, VoteResponse.Success> {
        return try {
            val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
            val document = session.document(voteRequest.sessionId)
                .collection(SessionDataModel.VOTATIONS)
                .document(voteRequest.votingTitle)
            val votation = document.get().await()

            if (!votation.exists()) {
                Either.Left(VoteResponse.Error.VotationNotFound)
            } else {
                val participantVotationValue = mapOf(voteRequest.uid to voteRequest.score)
                val participantVotation =
                    mapOf(VotingDataModel.PARTICIPANT_VOTATION to participantVotationValue)

                document.set(participantVotation, SetOptions.merge())
                Either.Right(VoteResponse.Success)
            }
        } catch (e: FirebaseFirestoreException) {
            Either.Left(VoteResponse.Error.RemoteException)
        }
    }
}
