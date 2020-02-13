package com.peakacard.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.bindView

class GridActivity : AppCompatActivity() {

    private val recyclerView: RecyclerView by bindView(R.id.cards)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = CardsAdapter(Card.values())
    }
}