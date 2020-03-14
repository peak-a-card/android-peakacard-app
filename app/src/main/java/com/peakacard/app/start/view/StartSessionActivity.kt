package com.peakacard.app.start.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.peakacard.app.R
import com.peakacard.app.start.view.model.CodeUiModel
import com.peakacard.app.start.view.model.NameUiModel
import com.peakacard.app.start.view.state.StartSessionState
import com.peakacard.core.ui.extensions.bindView
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartSessionActivity : AppCompatActivity(), StartSessionView {

    private val startSessionViewModel: StartSessionViewModel by viewModel()

    private val sessionName: TextInputEditText by bindView(R.id.start_session_name)
    private val sessionCode: TextInputEditText by bindView(R.id.start_session_code)
    private val sessionButton: MaterialButton by bindView(R.id.start_session_button)
    private val sessionError: TextView by bindView(R.id.start_session_error)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startsession)

        startSessionViewModel.bindView(this)
        configureButton()
    }

    private fun configureButton() {
        bindProgressButton(sessionButton)
        sessionButton.attachTextChangeAnimator()
        sessionButton.setOnClickListener {
            sessionError.isGone = true
            startSessionViewModel.startSession(
                NameUiModel(sessionName.text.toString()),
                CodeUiModel(sessionCode.text.toString())
            )
        }
    }

    override fun updateState(state: StartSessionState) {
        when (state) {
            StartSessionState.StartingSession -> {
                sessionButton.showProgress {
                    buttonTextRes = R.string.start_session_joining
                    progressColorRes = R.color.background
                }
            }
            StartSessionState.Started -> {
                sessionButton.hideProgress(R.string.start_session_joined)
            }
            is StartSessionState.Error -> {
                when (state) {
                    StartSessionState.Error.NameRequiredError -> {
                        sessionName.error = getString(R.string.start_session_error_name_required_error)
                    }
                    StartSessionState.Error.CodeRequiredError -> {
                        sessionCode.error = getString(R.string.start_session_error_code_required_error)
                    }
                    StartSessionState.Error.NoSessionFound -> {
                        sessionError.apply {
                            text = getString(R.string.start_session_error_no_session)
                            isVisible = true
                        }
                    }
                    StartSessionState.Error.Unspecified -> {
                        sessionError.apply {
                            text = getString(R.string.start_session_error_unspecified)
                            isVisible = true
                        }
                    }
                }
                sessionButton.hideProgress(R.string.start_session_enter)
            }
        }
    }
}