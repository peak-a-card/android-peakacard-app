package com.peakacard.host.common.navigator

import android.app.Activity
import android.content.Intent
import com.peakacard.host.R
import com.peakacard.host.voting.view.CreateVotingActivity
import com.peakacard.host.voting.view.VotingResultActivity
import com.peakacard.host.voting.view.WaitingVotesActivity

class HostNavigator {

  fun goToCreatingVoting(activity: Activity) {
    with(activity) {
      startActivity(
        Intent(
          this,
          CreateVotingActivity::class.java
        )
      )
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun goToWaitingVotes(activity: Activity) {
    with(activity) {
      val intent = Intent(this, WaitingVotesActivity::class.java)
      startActivity(intent)
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun goToVotingResult(activity: Activity, title: String) {
    with(activity) {
      val intent = Intent(this, VotingResultActivity::class.java).apply {
        putExtra(VotingResultActivity.EXTRA_VOTING_TITLE, title)
      }
      startActivity(intent)
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_right,
        R.anim.transition_slide_to_left
      )
    }
  }

  fun goBackToCreateVoting(activity: Activity) {
    with(activity) {
      startActivity(Intent(this, CreateVotingActivity::class.java))
      finish()
      overridePendingTransition(
        R.anim.transition_slide_from_left,
        R.anim.transition_slide_to_right
      )
    }
  }
}
