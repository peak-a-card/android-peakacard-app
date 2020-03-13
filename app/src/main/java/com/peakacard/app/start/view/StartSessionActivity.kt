package com.peakacard.app.start.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
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
    private val sessionButton: Button by bindView(R.id.start_session_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_startsession)

        startSessionViewModel.bindView(this)

        sessionButton.setOnClickListener {
            startSessionViewModel.startSession(
                NameUiModel(sessionName.text.toString()),
                CodeUiModel(sessionCode.text.toString())
            )
        }
    }

    override fun updateState(state: StartSessionState) {
        when (state) {
            StartSessionState.StartingSession -> TODO()
            StartSessionState.Started -> TODO()
            StartSessionState.Error.NameRequiredError -> {
                sessionName.error = getString(R.string.start_session_error_name_required_error)
            }
            StartSessionState.Error.CodeRequiredError -> {
                sessionCode.error = getString(R.string.start_session_error_code_required_error)
            }
        }
    }
}