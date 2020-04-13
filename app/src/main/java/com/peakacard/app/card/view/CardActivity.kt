package com.peakacard.app.card.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.R
import com.peakacard.app.cards.view.model.CardUiModel
import com.peakacard.app.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView

class CardActivity : AppCompatActivity() {

    private val cardDetailBackground: View by bindView(R.id.card_detail_background)
    private val cardDetail: EmojiTextView by bindView(R.id.card_detail)

    private val cardUiModel: CardUiModel by lazy { intent.extras?.get(EXTRA_CARD) as CardUiModel }
    private val transitionName: String by lazy { intent.extras?.get(EXTRA_TRANSITION_NAME) as String }

    companion object {
        private const val EXTRA_CARD = "EXTRA_CARD"
        private const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
        fun newIntent(context: Context, cardUiModel: CardUiModel): Intent {
            return Intent(context, CardActivity::class.java)
                .apply {
                    putExtra(EXTRA_CARD, cardUiModel)
                    putExtra(EXTRA_TRANSITION_NAME, cardUiModel.name)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        cardDetailBackground.setOnClickListener { onBackPressed() }

        cardDetail.applyCardText(cardUiModel)
        cardDetail.transitionName = this@CardActivity.transitionName

        CardDragHandler.makeDraggable(
            cardDetail,
            resources.getDimensionPixelSize(R.dimen.card_detail_height)
        ) {
            Toast.makeText(this, "Card as been sent!", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }
}
