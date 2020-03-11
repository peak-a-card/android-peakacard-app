package com.peakacard.app.cards.view

import android.animation.AnimatorInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.emoji.widget.EmojiTextView
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.cards.view.model.Card
import com.peakacard.app.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.inflate

class CardsAdapter(private val cards: List<Card>, private val listener: (Card, View) -> Unit) :
    RecyclerView.Adapter<CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(parent.inflate(R.layout.card_item), listener)
    }

    override fun getItemCount() = cards.size

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cards[position]
        ViewCompat.setTransitionName(holder.itemView, card.name)
        holder.bind(card, position)
    }
}

class CardViewHolder(itemView: View, private val listener: (Card, View) -> Unit) :
    RecyclerView.ViewHolder(itemView) {
    private val cardAnimationOut =
        AnimatorInflater.loadAnimator(itemView.context, R.animator.card_flip_out)
    private val cardAnimationIn =
        AnimatorInflater.loadAnimator(itemView.context, R.animator.card_flip_in)
    private val delay = itemView.context.resources.getInteger(R.integer.anim_delay)
    private val cardDisplayFront: EmojiTextView by bindView(R.id.card_display_front)
    private val cardDisplayBack: View by bindView(R.id.card_display_back)

    fun bind(card: Card, position: Int) {
        cardAnimationOut.setTarget(cardDisplayBack)
        cardAnimationIn.setTarget(cardDisplayFront)
        val delayByPosition = (delay * (position + 1) - (position * 100)).toLong()
        cardAnimationOut.apply { startDelay = delayByPosition }.start()
        cardAnimationIn.apply { startDelay = delayByPosition }.start()
        cardDisplayFront.applyCardText(card)
        itemView.setOnClickListener { listener(card, cardDisplayFront) }
    }
}