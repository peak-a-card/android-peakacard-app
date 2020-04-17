package com.peakacard.host.session.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.google.android.material.button.MaterialButton
import com.peakacard.core.ui.extensions.bindView
import com.peakacard.host.R

class CreateSessionActivity : AppCompatActivity() {

    private val createSessionTitle: TextView by bindView(R.id.create_session_title)
    private val createSessionCode: TextView by bindView(R.id.create_session_code)
    private val createSessionButton: MaterialButton by bindView(R.id.create_session_button)
    private val createSessionError: TextView by bindView(R.id.create_session_error)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createsession)
    }

    override fun onStart() {
        super.onStart()
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
}
