package com.peakacard.voting.data.datasource.remote

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.peakacard.core.Either
import com.peakacard.core.data.remote.model.PeakDataModel
import com.peakacard.session.data.datasource.remote.model.SessionDataModel
import com.peakacard.voting.data.datasource.remote.model.ParticipantVotationDataModel
import com.peakacard.voting.data.datasource.remote.model.ParticipantsVotationRequest
import com.peakacard.voting.data.datasource.remote.model.ParticipantsVotationResponse
import com.peakacard.voting.data.datasource.remote.model.VotingDataModel
import com.peakacard.voting.data.datasource.remote.model.VotingResponse
import com.peakacard.voting.data.datasource.remote.model.VotingStatusResponse
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.tasks.await

class VotingRemoteDataSource(private val database: FirebaseFirestore) {

  suspend fun listenStartedVoting(sessionId: String): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
    return callbackFlow {
      val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
      val votations = session.document(sessionId).collection(SessionDataModel.VOTATIONS)
        .orderBy(VotingDataModel.CREATION_DATE, Query.Direction.DESCENDING)

      val subscription = votations.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
          offer(Either.Left(VotingStatusResponse.Error.RemoteException))
        } else {
          val documentChange = snapshot?.documentChanges?.firstOrNull()
          if (documentChange?.type == DocumentChange.Type.ADDED) {
            offer(buildVoteStatus(snapshot.documents, VotingDataModel.Status.STARTED))
          }
        }
      }

      awaitClose { subscription.remove() }
    }
  }

  suspend fun listenEndedVoting(
    sessionId: String,
    votationTitle: String
  ): Flow<Either<VotingStatusResponse.Error, VotingStatusResponse.Success>> {
    return callbackFlow {
      val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
      val votations = session.document(sessionId).collection(SessionDataModel.VOTATIONS)
        .orderBy(VotingDataModel.CREATION_DATE, Query.Direction.DESCENDING)

      val subscription = votations.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
          offer(Either.Left(VotingStatusResponse.Error.RemoteException))
        } else {
          val documentChange = snapshot?.documentChanges?.firstOrNull()
          if (documentChange?.type == DocumentChange.Type.MODIFIED) {
            offer(buildVoteStatus(snapshot.documents, VotingDataModel.Status.ENDED))
          }
        }
      }

      awaitClose { subscription.remove() }
    }.filter {
      it.fold(
        { return@fold true },
        { votingSuccess -> return@fold votingSuccess.votingTitle == votationTitle }
      )
    }
  }

  private fun buildVoteStatus(
    votingDocuments: List<DocumentSnapshot>,
    status: VotingDataModel.Status
  ): Either<VotingStatusResponse.Error, VotingStatusResponse.Success> {
    val votingsByStatus = votingDocuments
      .map { it.toObject(VotingDataModel::class.java) }
      .filter { VotingDataModel.Status.fromString(it?.status) == status }

    return if (votingsByStatus.isNullOrEmpty()) {
      when (status) {
        VotingDataModel.Status.STARTED -> Either.Left(VotingStatusResponse.Error.NoVotingStarted)
        VotingDataModel.Status.ENDED -> Either.Left(VotingStatusResponse.Error.NoVotingEnded)
      }
    } else {
      val votingDataModel = votingsByStatus.firstOrNull()
      if (votingDataModel != null) {
        Either.Right(VotingStatusResponse.Success(votingDataModel.name))
      } else {
        Either.Left(VotingStatusResponse.Error.NoVotingStarted)
      }
    }
  }

  @Suppress("UNCHECKED_CAST")
  suspend fun listenForParticipantsVotation(participantsVotationRequest: ParticipantsVotationRequest):
    Flow<Either<ParticipantsVotationResponse.Error, ParticipantsVotationResponse.Success>> {
    return callbackFlow {
      val session = database.collection(PeakDataModel.ROOT_COLLECTION_ID)
      val votation = session.document(participantsVotationRequest.sessionId)
        .collection(SessionDataModel.VOTATIONS)
        .document(participantsVotationRequest.votationTitle)

      val subscription = votation.addSnapshotListener { snapshot, exception ->
        if (exception != null) {
          offer(Either.Left(ParticipantsVotationResponse.Error.RemoteException))
        } else {
          val participantsVotation = snapshot?.get(VotingDataModel.PARTICIPANT_VOTATION) as? Map<String, Float>

          if (participantsVotation == null) {
            offer(Either.Right(ParticipantsVotationResponse.Success(emptyList<ParticipantVotationDataModel>())))
          } else {
            val participantVotationDataModels = participantsVotation.map { (key, value) -> ParticipantVotationDataModel(key, value) }
            offer(Either.Right(ParticipantsVotationResponse.Success(participantVotationDataModels)))
          }
        }
      }

      awaitClose { subscription.remove() }
    }
  }

  suspend fun createVoting(
    sessionId: String,
    title: String
  ): Either<VotingResponse.RemoteError, VotingResponse.Success> {
    val votationValues = mapOf(
      VotingDataModel.CREATION_DATE to Timestamp.now(),
      VotingDataModel.NAME to title,
      VotingDataModel.STATUS to VotingDataModel.Status.STARTED.toString(),
      VotingDataModel.PARTICIPANT_VOTATION to emptyMap<String, Float>()
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

  suspend fun endVoting(
    sessionId: String,
    title: String
  ): Either<VotingResponse.RemoteError, VotingResponse.Success> {
    return try {
      database.collection(PeakDataModel.ROOT_COLLECTION_ID)
        .document(sessionId).collection(SessionDataModel.VOTATIONS)
        .document(title)
        .update(VotingDataModel.STATUS, VotingDataModel.Status.ENDED)
        .await()
      Either.Right(VotingResponse.Success)
    } catch (e: FirebaseFirestoreException) {
      Either.Left(VotingResponse.RemoteError)
    }
  }
}
