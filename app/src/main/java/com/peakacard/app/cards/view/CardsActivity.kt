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
import com.peakacard.app.cards.view.state.CardsViewState
import com.peakacard.core.ui.extensions.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CardsActivity : AppCompatActivity(), CardsView {

    private val cardsViewModel: CardsViewModel by viewModel()

    private val cardsGrid: GridRecyclerView by bindView(R.id.cards)
    private val cardsLoading: ProgressBar by bindView(R.id.cardsLoading)

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

        cardsGrid.apply {
            layoutManager = gridLayoutManagerBuilder.build()
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                this@CardsActivity,
                R.anim.cards_animation_from_bottom
            )
        }

        cardsViewModel.bindView(this)
    }

    override fun updateState(state: CardsViewState) {
        when (state) {
            CardsViewState.Loading -> {
                cardsLoading.isVisible = true
                cardsGrid.isGone = true
                cardsViewModel.getCards()
            }
            is CardsViewState.Loaded -> {
                cardsLoading.isGone = true
                cardsGrid.isVisible = true
                cardsGrid.adapter =
                    CardsAdapter(state.cards) { card, view ->
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
            CardsViewState.Empty -> Timber.d("Empty")
            CardsViewState.Error -> Timber.d("Error")
        }
    }
}