package com.peakacard.app.voting.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.voting.view.model.SessionParticipant
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class ParticipantsAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val participants: MutableSet<SessionParticipant> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.participant_item))
    }

    override fun getItemCount() = participants.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participants.elementAt(position))
    }

    fun addParticipant(participant: SessionParticipant) {
        if (participants.add(participant)) {
            notifyItemInserted(participants.size - 1)
        }
    }

    fun addParticipants(sessionParticipants: List<SessionParticipant>) {
        participants.clear()
        participants.addAll(sessionParticipants)
        notifyItemRangeChanged(0, participants.size)
    }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView by bindView(R.id.participant_name)

    fun bind(participant: SessionParticipant) {
        name.text = participant.name
    }
}
