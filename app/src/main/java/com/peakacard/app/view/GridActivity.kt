package com.peakacard.app.view

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.peakacard.app.view.model.Card
import com.peakacard.app.R
import com.peakacard.core.ui.bindView

class GridActivity : AppCompatActivity() {

    private val recyclerView: GridRecyclerView by bindView(
        R.id.cards
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid)

        recyclerView.apply {
            layoutManager = GridLayoutManager(this@GridActivity, 3)
            layoutAnimation = AnimationUtils.loadLayoutAnimation(
                this@GridActivity,
                R.anim.cards_animation_from_bottom
            )
            adapter =
                CardsAdapter(Card.values())
        }
    }
}