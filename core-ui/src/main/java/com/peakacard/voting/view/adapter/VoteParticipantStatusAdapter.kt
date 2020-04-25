package com.peakacard.voting.view.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.voting.view.model.VoteParticipantStatusUiModel
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate
import com.peakacard.core.ui.R as RCoreUi

class VoteParticipantStatusAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

  private val participantStatusUiModels: MutableSet<VoteParticipantStatusUiModel> = mutableSetOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
    return ParticipantViewHolder(parent.inflate(RCoreUi.layout.vote_participant_status_item))
  }

  override fun getItemCount() = participantStatusUiModels.size

  override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
    holder.bind(participantStatusUiModels.elementAt(position))
  }

  fun setParticipants(sessionParticipantStatusUiModels: List<VoteParticipantStatusUiModel>) {
    participantStatusUiModels.clear()
    notifyDataSetChanged()

    sessionParticipantStatusUiModels.forEach { sessionParticipantUiModel ->
      if (participantStatusUiModels.add(sessionParticipantUiModel)) {
        notifyItemInserted(participantStatusUiModels.size - 1)
      }
    }
  }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  private val name: TextView by bindView(RCoreUi.id.participant_name)
  private val progress: View by bindView(RCoreUi.id.participant_progress)
  private val check: View by bindView(RCoreUi.id.participant_check)

  fun bind(participantStatusUiModel: VoteParticipantStatusUiModel) {
    name.text = participantStatusUiModel.name
    when (participantStatusUiModel) {
      is VoteParticipantStatusUiModel.Voted -> {
        progress.isGone = true
        check.isVisible = true
      }
      is VoteParticipantStatusUiModel.Waiting -> {
        check.isGone = true
        progress.isVisible = true
      }
    }
  }
}
