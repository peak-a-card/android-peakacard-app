package com.peakacard.voting.view.adapter.item

import androidx.emoji.widget.EmojiTextView
import com.peakacard.card.view.model.CardUiModel
import com.peakacard.core.ui.extensions.applyCardText
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.peakacard.core.ui.R as RCoreUi

class HeaderItemVote(private val cardUiModel: CardUiModel) : Item() {

  override fun getLayout() = RCoreUi.layout.vote_item_header

  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    val title: EmojiTextView = viewHolder.itemView as EmojiTextView
    title.applyCardText(cardUiModel)
  }

}
