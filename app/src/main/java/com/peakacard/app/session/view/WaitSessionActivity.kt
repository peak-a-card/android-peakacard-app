package com.peakacard.app.session.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.app.R
import com.peakacard.core.ui.extensions.bindView

class WaitSessionActivity : AppCompatActivity() {

    private val participantList: RecyclerView by bindView(R.id.participant_list)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waitsession)
    }
}
