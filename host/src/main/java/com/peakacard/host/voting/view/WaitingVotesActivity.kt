package com.peakacard.host.voting.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.host.R
import com.peakacard.host.voting.view.state.WaitingVotesState
import org.koin.android.ext.android.inject

class WaitingVotesActivity : AppCompatActivity(), WaitingVotesView {

    private val waitingVotesViewModel: WaitingVotesViewModel by inject()

    private val error: View by bindView(R.id.waiting_votes_error)
    private val votedParticipantList: RecyclerView by bindView(R.id.waiting_votes_participant_list)

    private val votedParticipantsAdapter: VotedParticipantsAdapter by lazy {
        VotedParticipantsAdapter()
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

    override fun updateState(state: WaitingVotesState) {

    }
}
