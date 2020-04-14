package com.peakacard.app.result.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
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
        votingResultViewModel.getParticipants()
    }

    override fun updateState(state: VotingResultState) {
        when (state) {
            is VotingResultState.ParticipantsLoaded -> {
                votingParticipantsAdapter.setParticipants(state.participantUiModels)
                votingResultViewModel.listenParticipantsVote()
            }
            VotingResultState.Error -> TODO()
        }
    }
}
