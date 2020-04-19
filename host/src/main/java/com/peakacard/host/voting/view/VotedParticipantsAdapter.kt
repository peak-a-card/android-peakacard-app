package com.peakacard.host.voting.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate
import com.peakacard.host.R
import com.peakacard.host.voting.view.model.VotedParticipantUiModel

class VotedParticipantsAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val participantUiModels: MutableSet<VotedParticipantUiModel> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.waiting_votes_participant_item))
    }

    override fun getItemCount() = participantUiModels.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participantUiModels.elementAt(position))
    }

    fun setParticipants(votedParticipantUiModels: List<VotedParticipantUiModel>) {
//        participantUiModels.clear()
//        notifyDataSetChanged()

        votedParticipantUiModels.forEach { sessionParticipantUiModel ->
            if (participantUiModels.add(sessionParticipantUiModel)) {
                notifyItemInserted(participantUiModels.size - 1)
            }
        }
    }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView by bindView(R.id.participant_name)

    fun bind(participantUiModel: VotedParticipantUiModel) {
        name.text = participantUiModel.name
    }
}
