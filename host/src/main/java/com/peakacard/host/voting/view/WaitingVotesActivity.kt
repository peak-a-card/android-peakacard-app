package com.peakacard.host.voting.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.detachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.host.R
import com.peakacard.host.common.navigator.HostNavigator
import com.peakacard.host.voting.view.state.WaitingVotesState
import com.peakacard.voting.view.adapter.VoteParticipantStatusAdapter
import org.koin.android.ext.android.inject

class WaitingVotesActivity : AppCompatActivity(), WaitingVotesView {

  private val waitingVotesViewModel: WaitingVotesViewModel by inject()
  private val hostNavigator: HostNavigator by inject()

  private val error: View by bindView(R.id.waiting_votes_error)
  private val votedParticipantList: RecyclerView by bindView(R.id.waiting_votes_participant_list)
  private val endVoteButton: MaterialButton by bindView(R.id.waiting_votes_end_button)

  private val votedParticipantsAdapter: VoteParticipantStatusAdapter by lazy {
    VoteParticipantStatusAdapter()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_waitingvotes)

    waitingVotesViewModel.bindView(this)
    votedParticipantList.apply {
      layoutManager = LinearLayoutManager(this@WaitingVotesActivity)
      itemAnimator = DefaultItemAnimator()
      adapter = votedParticipantsAdapter
    }
    waitingVotesViewModel.listenParticipantsVote()
  }

  override fun onStart() {
    super.onStart()
    bindProgressButton(endVoteButton)
    configureButton()
  }

  private fun configureButton() {
    endVoteButton.apply {
      text = getString(R.string.waiting_votes_end_button)
      attachTextChangeAnimator()
      setOnClickListener {
        waitingVotesViewModel.endVote()
      }
    }
  }

  override fun onPause() {
    endVoteButton.apply {
      detachTextChangeAnimator()
      setOnClickListener(null)
    }
    super.onPause()
  }

  override fun updateState(state: WaitingVotesState) {
    when (state) {
      is WaitingVotesState.ParticipantsLoaded -> {
        error.isGone = true
        votedParticipantsAdapter.setParticipants(state.uiModels)
      }
      WaitingVotesState.EndingVote -> {
        endVoteButton.showProgress {
          buttonTextRes = R.string.waiting_votes_ending_button
          progressColorRes = R.color.background
        }
      }
      is WaitingVotesState.VoteEnded -> {
        endVoteButton.hideProgress(R.string.waiting_votes_ended_button)
        hostNavigator.goToVotingResult(this, state.title)
      }
      WaitingVotesState.Error -> {
        error.isVisible = true
      }
    }
  }
}
