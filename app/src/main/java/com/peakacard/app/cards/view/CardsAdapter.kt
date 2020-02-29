package com.peakacard.app.cards.view

import android.animation.AnimatorInflater
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
        return CardViewHolder(parent.inflate(R.layout.card_item))
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position], position)
    }
}

class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cardAnimationOut =
        AnimatorInflater.loadAnimator(itemView.context, R.animator.card_flip_out)
    private val cardAnimationIn =
        AnimatorInflater.loadAnimator(itemView.context, R.animator.card_flip_in)
    private val delay = itemView.context.resources.getInteger(R.integer.anim_delay)
    private val cardDisplayFront: EmojiTextView by bindView(R.id.cardDisplayFront)
    private val cardDisplayBack: View by bindView(R.id.cardDisplayBack)

    fun bind(card: Card, position: Int) {
        cardAnimationOut.setTarget(cardDisplayBack)
        cardAnimationIn.setTarget(cardDisplayFront)
        cardAnimationOut.apply { startDelay = (delay * position).toLong() }.start()
        cardAnimationIn.apply { startDelay = (delay * position).toLong() }.start()
        when (card) {
            Card.INFINITE -> cardDisplayFront.text = card.display.fromHtmlCompat()
            else -> cardDisplayFront.text = card.display
        }
    }
}