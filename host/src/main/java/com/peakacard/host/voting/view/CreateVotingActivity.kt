package com.peakacard.host.voting.view

import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.detachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.hideKeyboard
import com.peakacard.host.R
import com.peakacard.host.common.navigator.HostNavigator
import com.peakacard.host.voting.view.state.CreateVotingState
import org.koin.android.ext.android.inject

class CreateVotingActivity : AppCompatActivity(), CreateVotingView {

  private val createVotingViewModel: CreateVotingViewModel by inject()
  private val hostNavigator: HostNavigator by inject()

  private val createVotingTitle: TextInputEditText by bindView(R.id.create_voting_title)
  private val createVotingError: TextView by bindView(R.id.create_voting_error)
  private val createVotingButton: MaterialButton by bindView(R.id.create_voting_button)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_createvoting)

    createVotingViewModel.bindView(this)
  }

  override fun onStart() {
    super.onStart()
    bindProgressButton(createVotingButton)
    configureButton()
  }

  private fun configureButton() {
    createVotingButton.apply {
      text = getString(R.string.create_voting_button)
      attachTextChangeAnimator()
      setOnClickListener {
        createVotingError.isGone = true
        createVotingViewModel.createVoting(createVotingTitle.text.toString())
      }
    }
  }

  override fun onPause() {
    createVotingButton.apply {
      detachTextChangeAnimator()
      setOnClickListener(null)
    }
    super.onPause()
  }

  override fun updateState(state: CreateVotingState) {
    when (state) {
      CreateVotingState.CreatingVoting -> {
        createVotingButton.showProgress {
          buttonTextRes = R.string.create_voting_button_creating
          progressColorRes = R.color.background
        }
      }
      CreateVotingState.VotingCreated -> {
        createVotingButton.apply {
          hideProgress(R.string.create_voting_button_created)
          hideKeyboard()
          setOnClickListener(null)
        }
        Handler().postDelayed({
          hostNavigator.goToWaitingVotes(this)
        }, 1000)
      }
      is CreateVotingState.Error -> {
        when (state) {
          CreateVotingState.Error.TitleRequiredError -> {
            createVotingTitle.error =
              getString(R.string.create_voting_error_title_required)
          }
          CreateVotingState.Error.NoSessionFound -> {
            createVotingError.text = getString(R.string.create_voting_error_no_session)
            Handler().postDelayed({ onBackPressed() }, 1000)
          }
          CreateVotingState.Error.Unspecified -> {
            createVotingError.text = getString(R.string.create_voting_error_unspecified)
          }
        }
        createVotingButton.hideProgress(R.string.create_voting_button)
      }
    }
  }

  override fun onBackPressed() {
    super.onBackPressed()
    overridePendingTransition(
      R.anim.transition_slide_from_left,
      R.anim.transition_slide_to_right
    )
  }
}
