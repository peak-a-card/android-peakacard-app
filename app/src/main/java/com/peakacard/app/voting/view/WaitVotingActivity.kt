package com.peakacard.app.voting.view

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
import com.peakacard.app.common.navigator.AppNavigator
import com.peakacard.app.voting.view.state.WaitVotingState
import com.peakacard.core.ui.extensions.bindView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class WaitVotingActivity : AppCompatActivity(), WaitVotingView {

  private val waitVotingViewModel: WaitVotingViewModel by viewModel()

  private val message: TextView by bindView(R.id.wait_voting_message)
  private val progress: View by bindView(R.id.wait_voting_progress)
  private val error: View by bindView(R.id.wait_voting_error)
  private val participantList: RecyclerView by bindView(R.id.participant_list)

  private val appNavigator: AppNavigator by inject()

  private val participantsAdapter: ParticipantsAdapter by lazy { ParticipantsAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_waitvoting)

    waitVotingViewModel.bindView(this)

    participantList.apply {
      layoutManager = LinearLayoutManager(this@WaitVotingActivity)
      itemAnimator = DefaultItemAnimator()
      adapter = participantsAdapter
    }
    waitVotingViewModel.listenForVotingToStart()
    waitVotingViewModel.listenParticipantsToJoin()
  }

  override fun updateState(state: WaitVotingState) {
    when (state) {
      WaitVotingState.WaitingVotingStart -> {
        progress.isVisible = true
        error.isGone = true
      }
      is WaitVotingState.VotingStarted -> {
        progress.isGone = true
        error.isGone = true
        message.text = getString(R.string.wait_voting_message, state.title)

        Handler().postDelayed({
          appNavigator.goToCards(this, state.title)
        }, 1000)
      }
      WaitVotingState.Error -> {
        progress.isGone = true
        error.isVisible = true
      }
      WaitVotingState.VotingLeft -> {
        super.onBackPressed()
        overridePendingTransition(
          R.anim.transition_slide_from_left,
          R.anim.transition_slide_to_right
        )
      }
      is WaitVotingState.WaitParticipantState -> updateParticipantState(state)
    }
  }

  private fun updateParticipantState(state: WaitVotingState.WaitParticipantState) {
    when (state) {
      is WaitVotingState.WaitParticipantState.ParticipantsLoaded -> {
        participantsAdapter.setParticipants(state.participantUiModels)
      }
      WaitVotingState.WaitParticipantState.Error -> {
        progress.isGone = true
        error.isVisible = true
      }
    }
  }

  override fun onBackPressed() {
    waitVotingViewModel.leaveSession()
  }
}
