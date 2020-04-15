package com.peakacard.app.recap.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
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

    private val participantsVoteAdapter by lazy { ParticipantsVoteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recap)

        recapViewModel.bindView(this)

        title.text = votingTitle
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
            }
            RecapState.Error -> error.isVisible = true
        }
    }
}