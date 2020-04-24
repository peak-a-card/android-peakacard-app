package com.peakacard.app.result.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.app.recap.view.RecapActivity
import com.peakacard.app.result.view.state.VotingResultState
import com.peakacard.core.ui.extensions.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel

class VotingResultActivity : AppCompatActivity(), VotingResultView {

  private val votingResultViewModel: VotingResultViewModel by viewModel()

  private val message: TextView by bindView(R.id.voting_result_title)
  private val error: View by bindView(R.id.voting_result_error)
  private val votingParticipantList: RecyclerView by bindView(R.id.voting_result_participant_list)

  private val votingParticipantsAdapter: VotingParticipantsAdapter by lazy {
    VotingParticipantsAdapter()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_votingresult)

    votingResultViewModel.bindView(this)
    votingParticipantList.apply {
      layoutManager = LinearLayoutManager(this@VotingResultActivity)
      itemAnimator = DefaultItemAnimator()
      adapter = votingParticipantsAdapter
    }
    votingResultViewModel.listenParticipantsVote()
    votingResultViewModel.listenForVotingToEnd()
  }

  override fun updateState(state: VotingResultState) {
    when (state) {
      is VotingResultState.ParticipantsLoaded -> {
        error.isGone = true
        votingParticipantsAdapter.setParticipants(state.uiModels)
      }
      VotingResultState.Error -> error.isVisible = true
      is VotingResultState.EndedVotingState -> updateVotingState(state)
    }
  }

  private fun updateVotingState(state: VotingResultState.EndedVotingState) {
    when (state) {
      VotingResultState.EndedVotingState.WaitingVotingEnd -> {
        error.isGone = true
      }
      is VotingResultState.EndedVotingState.VotingEnded -> {
        error.isGone = true
        message.text = getString(R.string.voting_result_ended_title, state.title)

        Handler().postDelayed({
          val intent = Intent(this, RecapActivity::class.java).apply {
            putExtra(RecapActivity.EXTRA_VOTING_TITLE, state.title)
          }
          startActivity(intent)
          finish()
          overridePendingTransition(
            R.anim.transition_slide_from_right,
            R.anim.transition_slide_to_left
          )
        }, 1000)
      }
      VotingResultState.EndedVotingState.Error -> error.isVisible = true
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    overridePendingTransition(
      R.anim.transition_slide_from_left,
      R.anim.transition_slide_to_right
    )
  }
}
