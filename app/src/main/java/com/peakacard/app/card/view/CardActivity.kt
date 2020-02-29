package com.peakacard.app.card.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.R
import com.peakacard.app.cards.view.model.Card
import com.peakacard.app.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.fromHtmlCompat

class CardActivity : AppCompatActivity() {

    private val cardDetail: EmojiTextView by bindView(R.id.cardDetail)

    private val card: Card by lazy { intent.extras?.get(EXTRA_CARD) as Card }
    private val transitionName: String by lazy { intent.extras?.get(EXTRA_TRANSITION_NAME) as String }

    companion object {
        private const val EXTRA_CARD = "EXTRA_CARD"
        private const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
        fun newIntent(context: Context, card: Card): Intent {
            return Intent(context, CardActivity::class.java)
                .apply {
                    putExtra(EXTRA_CARD, card)
                    putExtra(EXTRA_TRANSITION_NAME, card.name)
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        cardDetail.applyCardText(card)
        cardDetail.transitionName = this@CardActivity.transitionName
    }
}