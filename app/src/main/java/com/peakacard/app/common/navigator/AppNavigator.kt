package com.peakacard.app.common.navigator

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import com.peakacard.app.R
import com.peakacard.app.card.view.CardActivity
import com.peakacard.app.cards.view.CardsActivity
import com.peakacard.app.recap.view.RecapActivity
import com.peakacard.app.result.view.VotingResultActivity
import com.peakacard.app.session.view.JoinSessionActivity
import com.peakacard.app.voting.view.WaitVotingActivity
import com.peakacard.card.view.model.CardUiModel

class AppNavigator {

  fun goToWaitVote(activity: Activity) {
    with(activity) {
      startActivity(Intent(this, WaitVotingActivity::class.java))
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun goToCards(activity: Activity, title: String) {
    with(activity) {
      val intent = Intent(this, CardsActivity::class.java).apply {
        putExtra(CardsActivity.EXTRA_SESSION_TITLE, title)
      }
      startActivity(intent)
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun openCard(activity: Activity, card: CardUiModel, view: View, requestCode: Int) {
    with(activity) {
      val activityOptions =
        ActivityOptionsCompat.makeSceneTransitionAnimation(
          this,
          view,
          ViewCompat.getTransitionName(view).orEmpty()
        )
      startActivityForResult(
        CardActivity.newIntent(this, card),
        requestCode,
        activityOptions.toBundle()
      )
    }
  }

  fun goBackToJoinSession(activity: Activity) {
    with(activity) {
      val intent = Intent(this, JoinSessionActivity::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
      }
      startActivity(intent)
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_left,
        R.anim.transition_slide_to_right
      )
    }
  }

  fun goToVotingResult(activity: Activity) {
    with(activity) {
      val intent = Intent(this, VotingResultActivity::class.java)
      startActivity(intent)
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun goToRecap(activity: Activity, title: String) {
    with(activity) {
      val intent = Intent(this, RecapActivity::class.java).apply {
        putExtra(RecapActivity.EXTRA_VOTING_TITLE, title)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
      }
      startActivity(intent)
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }
}
