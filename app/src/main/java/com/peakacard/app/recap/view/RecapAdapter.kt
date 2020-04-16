package com.peakacard.app.recap.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.extensions.applyCardText
import com.peakacard.app.result.view.model.VotingResultParticipantUiModel
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class RecapAdapter : RecyclerView.Adapter<ParticipantViewHolder>() {

    private val items: MutableSet<VotingResultParticipantUiModel.Voted> = mutableSetOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParticipantViewHolder {
        return ParticipantViewHolder(parent.inflate(R.layout.recap_participant_item))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ParticipantViewHolder, position: Int) {
        holder.bind(items.elementAt(position))
    }

    fun setParticipants(participantVotes: List<VotingResultParticipantUiModel.Voted>) {
        this.items.addAll(participantVotes)
        notifyDataSetChanged()
    }
}

class ParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView by bindView(R.id.participant_name)
    private val score: EmojiTextView by bindView(R.id.participant_score)

    fun bind(participantVote: VotingResultParticipantUiModel.Voted) {
        name.text = participantVote.name
        score.applyCardText(participantVote.card)
    }
}
