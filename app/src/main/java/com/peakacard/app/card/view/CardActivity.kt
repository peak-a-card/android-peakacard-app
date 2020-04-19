package com.peakacard.app.card.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.R
import com.peakacard.card.view.model.CardUiModel
import com.peakacard.app.card.view.state.CardState
import com.peakacard.core.ui.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel

class CardActivity : AppCompatActivity(), CardView {

    private val cardViewModel: CardViewModel by viewModel()

    private val cardDetailBackground: View by bindView(R.id.card_detail_background)
    private val cardDetail: EmojiTextView by bindView(R.id.card_detail)

    private val cardUiModel: CardUiModel by lazy { intent.extras?.get(EXTRA_CARD) as CardUiModel }
    private val transitionName: String by lazy { intent.extras?.get(EXTRA_TRANSITION_NAME) as String }

    companion object {
        private const val EXTRA_CARD = "EXTRA_CARD"
        private const val EXTRA_TRANSITION_NAME = "EXTRA_TRANSITION_NAME"
        const val RESULT_CODE_SENT = 1005
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

        cardViewModel.bindView(this)

        cardDetailBackground.setOnClickListener { onBackPressed() }

        cardDetail.applyCardText(cardUiModel)
        cardDetail.transitionName = this@CardActivity.transitionName

        CardDragHandler.makeDraggable(
            cardDetail,
            resources.getDimensionPixelSize(R.dimen.card_detail_height)
        ) {
            Toast.makeText(this, "Vote has been sent!", Toast.LENGTH_SHORT).show()
            cardViewModel.sendCard(cardUiModel)
        }
    }

    override fun updateState(state: CardState) {
        when (state) {
            CardState.Sending -> {
                // TODO
            }
            CardState.Sent -> {
                setResult(RESULT_CODE_SENT)
                onBackPressed()
            }
        }
    }
}
