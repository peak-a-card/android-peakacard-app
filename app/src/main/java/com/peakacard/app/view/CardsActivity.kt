package com.peakacard.app.view

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.peakacard.app.R
import com.peakacard.app.view.state.CardsViewState
import com.peakacard.core.ui.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class CardsActivity : AppCompatActivity(), CardsView {

    private val cardsViewModel: CardsViewModel by viewModel()

    private val cardsGrid: GridRecyclerView by bindView(R.id.cards)
    private val cardsLoading: ProgressBar by bindView(R.id.cardsLoading)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        cardsGrid.apply {
            layoutManager = GridLayoutManager(this@CardsActivity, 3)
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
                cardsGrid.adapter = CardsAdapter(state.cards.toTypedArray())
            }
            CardsViewState.Empty -> Timber.d("Empty")
            CardsViewState.Error -> Timber.d("Error")
        }
    }
}