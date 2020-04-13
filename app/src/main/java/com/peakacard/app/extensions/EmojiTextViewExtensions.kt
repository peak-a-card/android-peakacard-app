package com.peakacard.app.extensions

import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.card.view.model.CardUiModel
import com.peakacard.core.ui.extensions.fromHtmlCompat

fun EmojiTextView.applyCardText(cardUiModel: CardUiModel) {
    text = when (cardUiModel) {
        CardUiModel.INFINITE -> cardUiModel.display.fromHtmlCompat()
        else -> cardUiModel.display
    }
}
