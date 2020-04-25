package com.peakacard.voting.view.adapter.item

import android.view.View
import androidx.core.view.isVisible
import androidx.emoji.widget.EmojiTextView
import com.peakacard.core.ui.extensions.applyCardText
import com.peakacard.voting.view.model.VoteResultCard
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.peakacard.core.ui.R as RCoreUi

class HeaderItemVote(private val voteResultCard: VoteResultCard) : Item() {

  override fun getLayout() = RCoreUi.layout.vote_item_header

  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    val title: EmojiTextView = viewHolder.itemView.findViewById(RCoreUi.id.vote_item_title)
    val modeIndicator: View = viewHolder.itemView.findViewById(RCoreUi.id.vote_item_mode_indicator)
    title.apply {
      applyCardText(voteResultCard.card)
      modeIndicator.isVisible = voteResultCard is VoteResultCard.Mode
    }
  }
}
