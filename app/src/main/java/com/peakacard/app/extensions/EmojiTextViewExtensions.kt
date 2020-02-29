package com.peakacard.app.extensions

import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.cards.view.model.Card
import com.peakacard.core.ui.extensions.fromHtmlCompat

fun EmojiTextView.applyCardText(card: Card) {
    text = when (card) {
        Card.INFINITE -> card.display.fromHtmlCompat()
        else -> card.display
    }
}