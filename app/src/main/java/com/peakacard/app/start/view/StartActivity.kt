package com.peakacard.app.start.view

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.peakacard.app.R
import com.peakacard.core.ui.extensions.bindView

class StartActivity : AppCompatActivity() {

    private val sessionName: TextInputEditText by bindView(R.id.start_session_name)
    private val sessionCode: TextInputEditText by bindView(R.id.start_session_code)
    private val sessionButton: Button by bindView(R.id.start_session_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        sessionButton.setOnClickListener {  }
    }
}