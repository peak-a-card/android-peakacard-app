package com.peakacard.host.session.view

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.button.MaterialButton
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.view.PeakView
import com.peakacard.host.R
import com.peakacard.host.session.view.state.CreateSessionState
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateSessionActivity : AppCompatActivity(), PeakView<CreateSessionState> {

    private val createSessionViewModel: CreateSessionViewModel by viewModel()

    private val createSessionTitle: TextView by bindView(R.id.create_session_title)
    private val createSessionCode: TextView by bindView(R.id.create_session_code)
    private val createSessionButton: MaterialButton by bindView(R.id.create_session_button)
    private val createSessionError: View by bindView(R.id.create_session_error)
    private val createSessionProgress: View by bindView(R.id.create_session_progress)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createsession)
    }

    override fun onStart() {
        super.onStart()

        createSessionViewModel.bindView(this)

        bindProgressButton(createSessionButton)
        configureButton()
    }

    private fun configureButton() {
        createSessionButton.apply {
            text = getString(R.string.create_session_enter)
            attachTextChangeAnimator()
            setOnClickListener {
                createSessionError.isGone = true
            }
        }
    }

    override fun updateState(state: CreateSessionState) {
        when (state) {
            CreateSessionState.GeneratingSessionId -> {
                createSessionError.isGone = true
                createSessionCode.isGone = true
                createSessionProgress.isVisible = true
            }
            is CreateSessionState.SessionIdGenerated -> {
                createSessionError.isGone = true
                createSessionProgress.isGone = true
                createSessionCode.text = state.sessionId
            }
            CreateSessionState.Error -> {
                createSessionCode.isGone = true
                createSessionProgress.isGone = true
                createSessionError.isVisible = true
            }
        }
    }
}
