package com.peakacard.app.cards.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.peakacard.app.R
import com.peakacard.app.card.view.CardActivity
import com.peakacard.app.cards.view.state.CardsState
import com.peakacard.app.common.navigator.AppNavigator
import com.peakacard.core.ui.extensions.bindView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

private const val REQUEST_CODE_CARD = 1004

class CardsActivity : AppCompatActivity(), CardsView {

  companion object {
    const val EXTRA_SESSION_TITLE = "EXTRA_SESSION_TITLE"
  }

  private val cardsViewModel: CardsViewModel by viewModel()

  private val cardsGrid: GridRecyclerView by bindView(R.id.cards)
  private val cardsLoading: ProgressBar by bindView(R.id.cards_loading)

  private val appNavigator: AppNavigator by inject()

  private val sessionTitle by lazy { intent.getStringExtra(EXTRA_SESSION_TITLE) }
  private val cardWidth by lazy {
    resources.getDimensionPixelSize(R.dimen.card_width) + (resources.getDimensionPixelSize(R.dimen.M) * 2)
  }
  private val gridLayoutManagerBuilder by lazy {
    GridLayoutManagerBuilder(this@CardsActivity, cardWidth)
  }
  private val cardsAdapter: CardsAdapter by lazy {
    CardsAdapter { card, view -> appNavigator.openCard(this, card, view, REQUEST_CODE_CARD) }
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
      adapter = cardsAdapter
    }

    cardsViewModel.bindView(this)
    cardsViewModel.getCards()
  }

  override fun updateState(state: CardsState) {
    when (state) {
      CardsState.Loading -> {
        cardsLoading.isVisible = true
        cardsGrid.isGone = true
      }
      is CardsState.Loaded -> {
        cardsLoading.isGone = true
        cardsGrid.isVisible = true
        cardsAdapter.setItems(state.cardUiModels)
      }
      CardsState.Empty -> Timber.d("Empty")
      is CardsState.Error -> handleError(state)
      CardsState.VotingLeft -> goBack()
    }
  }

  private fun goBack() {
    appNavigator.goBackToJoinSession(this)
  }

  private fun handleError(state: CardsState.Error) {
    when (state) {
      CardsState.Error.Unspecified -> Timber.d("Error")
      CardsState.Error.CouldNotLeaveVoting -> goBack()
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    when (requestCode) {
      REQUEST_CODE_CARD -> {
        if (resultCode == CardActivity.RESULT_CODE_SENT) {
          Handler().postDelayed({
            appNavigator.goToVotingResult(this)
          }, 1000)
        }
      }
      else -> super.onActivityResult(requestCode, resultCode, data)
    }
  }

  override fun onBackPressed() {
    cardsViewModel.leaveSession()
  }
}
