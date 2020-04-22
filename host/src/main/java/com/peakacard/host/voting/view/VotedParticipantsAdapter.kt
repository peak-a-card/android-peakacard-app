package com.peakacard.host.voting.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate
import com.peakacard.host.R
import com.peakacard.host.voting.view.model.VotedParticipantUiModel

class VotedParticipantsAdapter : RecyclerView.Adapter<VotedParticipantViewHolder>() {

  private val participantUiModels: MutableSet<VotedParticipantUiModel> = mutableSetOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotedParticipantViewHolder {
    return VotedParticipantViewHolder(parent.inflate(R.layout.waiting_votes_participant_item))
  }

  override fun getItemCount() = participantUiModels.size

  override fun onBindViewHolder(holder: VotedParticipantViewHolder, position: Int) {
    holder.bind(participantUiModels.elementAt(position))
  }

  fun setParticipants(votedParticipantUiModels: List<VotedParticipantUiModel>) {
    votedParticipantUiModels.forEach { sessionParticipantUiModel ->
      if (participantUiModels.add(sessionParticipantUiModel)) {
        notifyItemInserted(participantUiModels.size - 1)
      }
    }
  }
}

class VotedParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  private val name: TextView by bindView(R.id.participant_name)

  fun bind(participantUiModel: VotedParticipantUiModel) {
    name.text = participantUiModel.name
  }
}
