package com.peakacard.app.view

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class GridLayoutManagerBuilder(private val context: Context, private val columnWidth: Int) {
    private val noColumns: Int
        get() {
            val resources = context.resources
            val displayMetrics = resources.displayMetrics
            return displayMetrics.widthPixels / columnWidth
        }

    fun build() = GridLayoutManager(context, noColumns)
}