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
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.host.R
import com.peakacard.host.voting.view.state.VotingResultState
import org.koin.androidx.viewmodel.ext.android.viewModel

class VotingResultActivity : AppCompatActivity(), VotingResultView {

    companion object {
        const val EXTRA_VOTING_TITLE = "EXTRA_VOTING_TITLE"
    }

    private val votingResultViewModel: VotingResultViewModel by viewModel()

    private val title: TextView by bindView(R.id.voting_result_title)
    private val error: View by bindView(R.id.voting_result_error)
    private val participantsList: RecyclerView by bindView(R.id.voting_result_participant_list)

    private val votingTitle by lazy { intent.getStringExtra(EXTRA_VOTING_TITLE) }

    private val participantsVoteAdapter by lazy { VotingResultAdapter() }

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

        votingResultViewModel.getVotingResult()
    }

    override fun updateState(state: VotingResultState) {
        when (state) {
            is VotingResultState.VotationsLoaded -> {
                error.isGone = true
                participantsVoteAdapter.setParticipants(state.uiModels)
            }
            VotingResultState.Error -> error.isVisible = true
        }
    }
}
