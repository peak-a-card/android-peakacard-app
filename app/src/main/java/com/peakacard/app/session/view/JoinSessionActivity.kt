package com.peakacard.app.session.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import com.peakacard.app.cards.view.CardsActivity
import com.peakacard.app.session.view.model.CodeUiModel
import com.peakacard.app.session.view.model.NameUiModel
import com.peakacard.app.session.view.state.JoinSessionState
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.hideKeyboard
import org.koin.androidx.viewmodel.ext.android.viewModel

class JoinSessionActivity : AppCompatActivity(), JoinSessionView {

    private val joinSessionViewModel: JoinSessionViewModel by viewModel()

    private val joinSessionName: TextInputEditText by bindView(R.id.join_session_name)
    private val joinSessionCode: TextInputEditText by bindView(R.id.join_session_code)
    private val joinSessionButton: MaterialButton by bindView(R.id.join_session_button)
    private val joinSessionError: TextView by bindView(R.id.join_session_error)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joinsession)

        joinSessionViewModel.bindView(this)
        configureButton()
    }

    private fun configureButton() {
        bindProgressButton(joinSessionButton)
        joinSessionButton.attachTextChangeAnimator()
        joinSessionButton.setOnClickListener {
            joinSessionError.isGone = true
            joinSessionViewModel.joinSession(
                NameUiModel(joinSessionName.text.toString()),
                CodeUiModel(joinSessionCode.text.toString())
            )
        }
    }

    override fun updateState(state: JoinSessionState) {
        when (state) {
            JoinSessionState.JoiningSession -> {
                joinSessionButton.showProgress {
                    buttonTextRes = R.string.join_session_joining
                    progressColorRes = R.color.background
                }
            }
            JoinSessionState.Joined -> {
                joinSessionButton.hideProgress(R.string.join_session_joined)
                joinSessionButton.hideKeyboard()
                Handler().postDelayed({
                    val intent = Intent(this, CardsActivity::class.java).apply {
                        putExtra(CardsActivity.EXTRA_SESSION_TITLE, joinSessionCode.text.toString())
                    }
                    startActivity(intent)
                    finish()
                    overridePendingTransition(
                        R.anim.transition_slide_from_right,
                        R.anim.transition_slide_to_left
                    )
                }, 1000)
            }
            is JoinSessionState.Error -> {
                when (state) {
                    JoinSessionState.Error.NameRequiredError -> {
                        joinSessionName.error =
                            getString(R.string.join_session_error_name_required_error)
                    }
                    JoinSessionState.Error.CodeRequiredError -> {
                        joinSessionCode.error =
                            getString(R.string.join_session_error_code_required_error)
                    }
                    JoinSessionState.Error.NoSessionFound -> {
                        joinSessionError.apply {
                            text = getString(R.string.join_session_error_no_session)
                            isVisible = true
                        }
                    }
                    JoinSessionState.Error.Unspecified -> {
                        joinSessionError.apply {
                            text = getString(R.string.join_session_error_unspecified)
                            isVisible = true
                        }
                    }
                }
                joinSessionButton.hideProgress(R.string.join_session_enter)
            }
        }
    }
}