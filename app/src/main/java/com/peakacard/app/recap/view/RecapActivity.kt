package com.peakacard.app.recap.view

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
import com.peakacard.app.cards.view.CardsActivity
import com.peakacard.app.recap.view.state.RecapState
import com.peakacard.core.ui.extensions.bindView
import org.koin.android.ext.android.inject

class RecapActivity : AppCompatActivity(), RecapView {

  companion object {
    const val EXTRA_VOTING_TITLE = "EXTRA_VOTING_TITLE"
  }

  private val recapViewModel: RecapViewModel by inject()

  private val title: TextView by bindView(R.id.recap_title)
  private val error: View by bindView(R.id.recap_error)
  private val participantsList: RecyclerView by bindView(R.id.recap_participant_list)

  private val votingTitle by lazy { intent.getStringExtra(EXTRA_VOTING_TITLE) }

  private val participantsVoteAdapter by lazy { RecapAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_recap)

    recapViewModel.bindView(this)

    title.text = getString(R.string.recap_title, votingTitle)
    participantsList.apply {
      layoutManager = LinearLayoutManager(this@RecapActivity)
      itemAnimator = DefaultItemAnimator()
      adapter = participantsVoteAdapter
    }

    recapViewModel.getVotingResult()
  }

  override fun updateState(state: RecapState) {
    when (state) {
      is RecapState.VotationsLoaded -> {
        error.isGone = true
        participantsVoteAdapter.setParticipants(state.uiModels)
        recapViewModel.listenForVotingToStart()
      }
      is RecapState.VotingStarted -> {
        error.isGone = true
        Handler().postDelayed({
          val intent = Intent(this, CardsActivity::class.java).apply {
            putExtra(CardsActivity.EXTRA_SESSION_TITLE, state.title)
          }
          startActivity(intent)
          finish()
          overridePendingTransition(
            R.anim.transition_slide_from_right,
            R.anim.transition_slide_to_left
          )
        }, 1000)
      }
      RecapState.Error -> error.isVisible = true
    }
  }
}
