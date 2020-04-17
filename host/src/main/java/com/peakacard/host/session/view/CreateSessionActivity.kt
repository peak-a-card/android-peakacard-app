package com.peakacard.host.session.view

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.core.ui.extensions.hideKeyboard
import com.peakacard.core.view.PeakView
import com.peakacard.host.R
import com.peakacard.host.session.view.state.CreateSessionState
import com.peakacard.session.view.model.mapper.FirebaseUserMapper
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

private const val RC_SIGN_IN = 1005

class CreateSessionActivity : AppCompatActivity(), PeakView<CreateSessionState> {

    private val createSessionViewModel: CreateSessionViewModel by viewModel()

    private val firebaseUserMapper: FirebaseUserMapper by inject()

    private val createSessionTitle: TextView by bindView(R.id.create_session_title)
    private val createSessionCode: TextView by bindView(R.id.create_session_code)
    private val createSessionButton: MaterialButton by bindView(R.id.create_session_button)
    private val createSessionError: TextView by bindView(R.id.create_session_error)
    private val createSessionProgress: View by bindView(R.id.create_session_progress)

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
        setContentView(R.layout.activity_createsession)
        createSessionViewModel.bindView(this)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            Timber.d("User already logged with account ${currentUser.email}")
            createSessionTitle.text =
                getString(R.string.create_session_title_logged, currentUser.displayName)
        } else {
            createSessionTitle.text = getString(R.string.create_session_title)
        }
        bindProgressButton(createSessionButton)
        configureButton()
    }

    private fun configureButton() {
        createSessionButton.apply {
            text = getString(R.string.create_session_enter)
            attachTextChangeAnimator()
            setOnClickListener {
                createSessionError.isGone = true
                val user = firebaseAuth.currentUser
                if (user != null) {
                    doCreateSession(user)
                } else {
                    startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
                }
            }
        }
    }

    override fun onPause() {
        createSessionButton.apply {
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
                        createSessionTitle.text =
                            getString(R.string.create_session_title_logged, user.displayName)
                        doCreateSession(user)
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

    private fun doCreateSession(user: FirebaseUser?) {
        createSessionViewModel.createSession(firebaseUserMapper.map(user))
    }

    private fun showSignInError() {
        createSessionCode.isGone = true
        createSessionProgress.isGone = true
        createSessionError.apply {
            text = getString(R.string.create_session_error_sign_in)
            isVisible = true
        }
    }

    override fun updateState(state: CreateSessionState) {
        when (state) {
            CreateSessionState.GeneratingSessionId -> {
                createSessionError.isGone = true
                createSessionCode.isGone = true
                createSessionProgress.isVisible = true
                createSessionButton.showProgress {
                    buttonTextRes = R.string.create_session_creating
                    progressColorRes = R.color.background
                }
            }
            is CreateSessionState.SessionIdGenerated -> {
                createSessionError.isGone = true
                createSessionProgress.isGone = true
                createSessionCode.text = state.sessionId
                createSessionCode.isVisible = true
                createSessionButton.hideProgress(R.string.create_session_created)
                createSessionButton.hideKeyboard()
                // TODO start activity
            }
            is CreateSessionState.Error -> {
                when (state) {
                    CreateSessionState.Error.UserSignIn -> showSignInError()
                    CreateSessionState.Error.CannotGenerateId -> {
                        createSessionCode.isGone = true
                        createSessionProgress.isGone = true
                        createSessionError.isVisible = true
                    }
                }
            }
        }
    }
}
