package com.peakacard.core.ui.extensions

import androidx.emoji.widget.EmojiTextView
import com.peakacard.card.view.model.CardUiModel

fun EmojiTextView.applyCardText(cardUiModel: CardUiModel) {
  text = when (cardUiModel) {
    CardUiModel.INFINITE -> cardUiModel.display.fromHtmlCompat()
    else -> cardUiModel.display
  }
}
