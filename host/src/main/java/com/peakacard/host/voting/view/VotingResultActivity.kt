package com.peakacard.host.voting.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.host.R
import com.peakacard.host.common.navigator.HostNavigator
import com.peakacard.host.voting.view.state.VotingResultState
import com.peakacard.voting.view.adapter.HeaderItemVote
import com.peakacard.voting.view.adapter.ItemParticipant
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class VotingResultActivity : AppCompatActivity(), VotingResultView {

  companion object {
    const val EXTRA_VOTING_TITLE = "EXTRA_VOTING_TITLE"
  }

  private val votingResultViewModel: VotingResultViewModel by viewModel()
  private val hostNavigator: HostNavigator by inject()

  private val title: TextView by bindView(R.id.voting_result_title)
  private val error: View by bindView(R.id.voting_result_error)
  private val participantsList: RecyclerView by bindView(R.id.voting_result_participant_list)
  private val startVoteButton: MaterialButton by bindView(R.id.voting_result_start_button)

  private val votingTitle by lazy { intent.getStringExtra(EXTRA_VOTING_TITLE) }

  private val participantsVoteAdapter by lazy { GroupAdapter<GroupieViewHolder>() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_votingresult)

    votingResultViewModel.bindView(this)

    title.text = getString(R.string.voting_result_title, votingTitle)
    participantsList.apply {
      layoutManager = LinearLayoutManager(this@VotingResultActivity)
      itemAnimator = DefaultItemAnimator()
      adapter = participantsVoteAdapter
    }

    startVoteButton.setOnClickListener {
      hostNavigator.goBackToCreateVoting(this)
    }

    votingResultViewModel.getVotingResult()
  }

  override fun updateState(state: VotingResultState) {
    when (state) {
      is VotingResultState.VotationsLoaded -> {
        error.isGone = true
        state.uiModels.forEach { voteParticipant ->
          participantsVoteAdapter.add(Section(HeaderItemVote(voteParticipant.card)).apply {
            voteParticipant.participants.forEach { participantName ->
              add(ItemParticipant(participantName))
            }
          })
        }
      }
      VotingResultState.Error -> error.isVisible = true
    }
  }
}
