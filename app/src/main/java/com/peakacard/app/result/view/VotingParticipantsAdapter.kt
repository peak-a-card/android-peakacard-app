package com.peakacard.app.result.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class VotingParticipantsAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val participantUiModels: MutableSet<SessionParticipantUiModel> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.voting_result_participant_item))
    }

    override fun getItemCount() = participantUiModels.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participantUiModels.elementAt(position))
    }

    fun setParticipants(sessionParticipantUiModels: List<SessionParticipantUiModel>) {
        participantUiModels.clear()
        notifyDataSetChanged()

        sessionParticipantUiModels.forEach { sessionParticipantUiModel ->
            if (participantUiModels.add(sessionParticipantUiModel)) {
                notifyItemInserted(participantUiModels.size - 1)
            }
        }
    }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView by bindView(R.id.participant_name)

    fun bind(participantUiModel: SessionParticipantUiModel) {
        name.text = participantUiModel.name
    }
}
