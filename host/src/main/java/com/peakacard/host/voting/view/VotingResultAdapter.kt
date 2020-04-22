package com.peakacard.host.voting.view

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.ui.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate
import com.peakacard.host.R
import com.peakacard.host.voting.view.model.VotingResultParticipantUiModel

class VotingResultAdapter : RecyclerView.Adapter<VotingResultParticipantViewHolder>() {

  private val items: MutableSet<VotingResultParticipantUiModel> = mutableSetOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VotingResultParticipantViewHolder {
    return VotingResultParticipantViewHolder(parent.inflate(R.layout.voting_result_participant_item))
  }

  override fun getItemCount() = items.size

  override fun onBindViewHolder(holder: VotingResultParticipantViewHolder, position: Int) {
    holder.bind(items.elementAt(position))
  }

  fun setParticipants(participantVotes: List<VotingResultParticipantUiModel>) {
    this.items.addAll(participantVotes)
    notifyDataSetChanged()
  }
}

class VotingResultParticipantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  private val name: TextView by bindView(R.id.participant_name)
  private val score: EmojiTextView by bindView(R.id.participant_score)

  fun bind(participantVote: VotingResultParticipantUiModel) {
    name.text = participantVote.name
    score.applyCardText(participantVote.card)
  }
}
