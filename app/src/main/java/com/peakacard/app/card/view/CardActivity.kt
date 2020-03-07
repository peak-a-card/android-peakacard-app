package com.peakacard.app.card.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.emoji.widget.EmojiTextView
import com.peakacard.app.R
import com.peakacard.app.cards.view.model.Card
import com.peakacard.app.extensions.applyCardText
import com.peakacard.core.ui.extensions.bindView

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

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        cardDetail.applyCardText(card)
        cardDetail.transitionName = this@CardActivity.transitionName

        val height = resources.getDimensionPixelSize(R.dimen.card_detail_height)
        val heightLimit = height * 0.6
        var dY = 0f
        var startY = 0f
        cardDetail.setOnTouchListener { view, motionEvent ->
            return@setOnTouchListener when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    startY = view.y
                    dY = view.y - motionEvent.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    view.y = motionEvent.rawY + dY
                    true
                }
                MotionEvent.ACTION_UP -> {
                    if (view.y <= -heightLimit) {
                        view.animate().y(-height.toFloat()).apply {
                            duration = resources.getInteger(R.integer.anim_duration_short).toLong()
                            interpolator = AccelerateDecelerateInterpolator()
                        }
                    } else {
                        view.animate().y(startY).apply {
                            duration = resources.getInteger(R.integer.anim_duration_short).toLong()
                            interpolator = AccelerateDecelerateInterpolator()
                        }
                    }
                    true
                }
                else -> false
            }
        }
    }
}