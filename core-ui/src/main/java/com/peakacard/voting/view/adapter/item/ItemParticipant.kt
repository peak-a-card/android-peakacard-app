package com.peakacard.voting.view.adapter.item

import android.widget.TextView
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import com.peakacard.core.ui.R as RCoreUi

class ItemParticipant(private val participantName: String) : Item() {

  override fun getLayout() = RCoreUi.layout.vote_participant_item

  override fun bind(viewHolder: GroupieViewHolder, position: Int) {
    val name: TextView = viewHolder.itemView as TextView
    name.text = participantName
  }
}
