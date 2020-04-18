package com.peakacard.app.session.view

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.github.razir.progressbutton.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.peakacard.app.R
import com.peakacard.app.session.view.state.JoinSessionState
import com.peakacard.app.voting.view.WaitVotingActivity
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.hideKeyboard
import com.peakacard.session.view.model.mapper.FirebaseUserMapper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

private const val RC_SIGN_IN = 1004

class JoinSessionActivity : AppCompatActivity(), JoinSessionView {

    private val joinSessionViewModel: JoinSessionViewModel by viewModel()

    private val firebaseUserMapper: FirebaseUserMapper by inject()

    private val joinSessionGreetings: TextView by bindView(R.id.join_session_greetings)
    private val joinSessionCode: TextInputEditText by bindView(R.id.join_session_code)
    private val joinSessionButton: MaterialButton by bindView(R.id.join_session_button)
    private val joinSessionError: TextView by bindView(R.id.join_session_error)

    private val googleSignInClient: GoogleSignInClient by lazy {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        GoogleSignIn.getClient(this, googleSignInOptions)
    }
    private val firebaseAuth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joinsession)

        joinSessionViewModel.bindView(this)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Timber.d("User already logged with account ${currentUser.email}")
            joinSessionGreetings.text =
                getString(R.string.join_session_greetings_logged, currentUser.displayName)
        } else {
            joinSessionGreetings.text = getString(R.string.join_session_greetings)
        }
        bindProgressButton(joinSessionButton)
        configureButton()
    }

    private fun configureButton() {
        joinSessionButton.apply {
            text = getString(R.string.join_session_enter)
            attachTextChangeAnimator()
            setOnClickListener {
                joinSessionError.isGone = true
                val user = firebaseAuth.currentUser
                if (user != null) {
                    doJoinSession(user)
                } else {
                    startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
                }
            }
        }
    }

    override fun onPause() {
        joinSessionButton.apply {
            detachTextChangeAnimator()
            setOnClickListener(null)
        }
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            RC_SIGN_IN -> {
                val account = try {
                    GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException::class.java)
                } catch (apiException: ApiException) {
                    Timber.e("Google signInResult:failed code=${apiException.statusCode}")
                    null
                }
                if (account == null) {
                    Timber.e("Error retrieving account")
                    showSignInError()
                } else {
                    firebaseAuthWithGoogle(account)
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        Timber.d("Id Token google: ${account.idToken}")
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("signInWithCredential:success")
                    val user = firebaseAuth.currentUser
                    if (user != null) {
                        Timber.d("User logged successfully with account ${user.email}")
                        joinSessionGreetings.text =
                            getString(R.string.join_session_greetings_logged, user.displayName)
                        doJoinSession(user)
                    } else {
                        showSignInError()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w(task.exception, "signInWithCredential:failure")
                    showSignInError()
                }
            }
    }

    private fun doJoinSession(user: FirebaseUser?) {
        joinSessionViewModel.joinSession(
            firebaseUserMapper.map(user),
            joinSessionCode.text.toString()
        )
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
                startActivity(Intent(this, WaitVotingActivity::class.java))
                overridePendingTransition(
                    R.anim.transition_slide_from_right,
                    R.anim.transition_slide_to_left
                )
            }
            is JoinSessionState.Error -> {
                when (state) {
                    JoinSessionState.Error.UserSignInError -> showSignInError()
                    JoinSessionState.Error.CodeRequiredError -> {
                        joinSessionCode.error =
                            getString(R.string.join_session_error_code_required)
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

    private fun showSignInError() {
        joinSessionError.apply {
            text = getString(R.string.join_session_error_sign_in)
            isVisible = true
        }
    }
}
