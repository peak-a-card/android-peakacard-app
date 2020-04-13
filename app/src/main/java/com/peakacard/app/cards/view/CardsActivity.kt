package com.peakacard.app.cards.view

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.peakacard.app.R
import com.peakacard.app.card.view.CardActivity
import com.peakacard.app.cards.view.state.CardsState
import com.peakacard.core.ui.extensions.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CardsActivity : AppCompatActivity(), CardsView {

    companion object {
        const val EXTRA_SESSION_TITLE = "EXTRA_SESSION_TITLE"
    }

    private val cardsViewModel: CardsViewModel by viewModel()

    private val cardsGrid: GridRecyclerView by bindView(R.id.cards)
    private val cardsLoading: ProgressBar by bindView(R.id.cards_loading)

    private val sessionTitle by lazy { intent.getStringExtra(EXTRA_SESSION_TITLE) }
    private val cardWidth by lazy {
        resources.getDimensionPixelSize(R.dimen.card_width) + (resources.getDimensionPixelSize(R.dimen.M) * 2)
    }
    private val gridLayoutManagerBuilder by lazy {
        GridLayoutManagerBuilder(
            this@CardsActivity,
            cardWidth
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards)

        supportActionBar?.title = getString(R.string.cards_session_title, sessionTitle)

        cardsGrid.apply {
            layoutManager = gridLayoutManagerBuilder.build()
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                this@CardsActivity,
                R.anim.cards_animation_from_bottom
            )
        }

        cardsViewModel.bindView(this)
    }

    override fun updateState(state: CardsState) {
        when (state) {
            CardsState.Loading -> {
                cardsLoading.isVisible = true
                cardsGrid.isGone = true
                cardsViewModel.getCards()
            }
            is CardsState.Loaded -> {
                cardsLoading.isGone = true
                cardsGrid.isVisible = true
                cardsGrid.adapter =
                    CardsAdapter(state.cardUiModels) { card, view ->
                        val activityOptions =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this,
                                view,
                                ViewCompat.getTransitionName(view).orEmpty()
                            )
                        startActivity(
                            CardActivity.newIntent(this, card),
                            activityOptions.toBundle()
                        )
                    }
            }
            CardsState.Empty -> Timber.d("Empty")
            CardsState.Error -> Timber.d("Error")
            CardsState.VotingLeft -> {
                super.onBackPressed()
                overridePendingTransition(
                    R.anim.transition_slide_from_left,
                    R.anim.transition_slide_to_right
                )
            }
        }
    }

    override fun onBackPressed() {
        cardsViewModel.leaveSession()
    }
}
