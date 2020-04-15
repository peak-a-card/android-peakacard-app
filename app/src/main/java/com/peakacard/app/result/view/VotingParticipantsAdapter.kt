package com.peakacard.app.result.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class VotingParticipantsAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val participantUiModels: MutableSet<VotingResultParticipantUiModel> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.voting_result_participant_item))
    }

    override fun getItemCount() = participantUiModels.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(participantUiModels.elementAt(position))
    }

    fun setParticipants(sessionParticipantUiModels: List<VotingResultParticipantUiModel>) {
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
    private val progress: View by bindView(R.id.participant_progress)
    private val check: View by bindView(R.id.participant_check)

    fun bind(participantUiModel: VotingResultParticipantUiModel) {
        name.text = participantUiModel.name
        when (participantUiModel) {
            is VotingResultParticipantUiModel.Voted -> {
                progress.isGone = true
                check.isVisible = true
            }
            is VotingResultParticipantUiModel.Waiting -> {
                check.isGone = true
                progress.isVisible = true
            }
        }
    }
}
