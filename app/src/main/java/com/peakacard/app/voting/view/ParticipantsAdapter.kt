package com.peakacard.app.voting.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.voting.view.model.SessionParticipantUiModel
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class ParticipantsAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val participantUiModels: MutableSet<SessionParticipantUiModel> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.participant_item))
    }

    override fun getItemCount() = participantUiModels.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participantUiModels.elementAt(position))
    }

    fun addParticipant(participantUiModel: SessionParticipantUiModel) {
        if (participantUiModels.add(participantUiModel)) {
            notifyItemInserted(participantUiModels.size - 1)
        }
    }

    fun addParticipants(sessionParticipantUiModels: List<SessionParticipantUiModel>) {
        participantUiModels.clear()
        participantUiModels.addAll(sessionParticipantUiModels)
        notifyItemRangeChanged(0, participantUiModels.size)
    }

    fun removeParticipant(participantUiModel: SessionParticipantUiModel) {
        val position = participantUiModels.indexOf(participantUiModel)
        if (participantUiModels.remove(participantUiModel)) {
            notifyItemRemoved(position)
        }
    }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView by bindView(R.id.participant_name)

    fun bind(participantUiModel: SessionParticipantUiModel) {
        name.text = participantUiModel.name
    }
}
