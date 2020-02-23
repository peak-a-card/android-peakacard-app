package com.peakacard.app.cards.view

import android.view.View
import android.view.ViewGroup
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.cards.view.model.Card
import com.peakacard.core.ui.bindView
import com.peakacard.core.ui.fromHtmlCompat
import com.peakacard.core.ui.inflate

class CardsAdapter(private val cards: List<Card>) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(
            parent.inflate(
                R.layout.card_item
            )
        )
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cardDisplay: EmojiTextView by bindView(R.id.cardDisplay)

    fun bind(card: Card) {
        when (card) {
            Card.INFINITE -> cardDisplay.text = card.display.fromHtmlCompat()
            else -> cardDisplay.text = card.display
        }
    }
}