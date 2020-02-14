package com.peakacard.app

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.GridLayoutAnimationController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    override fun attachLayoutAnimationParameters(
        child: View?,
        params: ViewGroup.LayoutParams?,
        index: Int,
        count: Int
    ) {
        if (adapter == null || layoutManager !is GridLayoutManager) {
            super.attachLayoutAnimationParameters(child, params, index, count)
        }

        val gridLayoutManager = layoutManager as GridLayoutManager
        val animationParameters =
            params?.layoutAnimationParameters as? GridLayoutAnimationController.AnimationParameters
                ?: GridLayoutAnimationController.AnimationParameters()

        val columns = gridLayoutManager.spanCount
        val invertedIndex = count - index
        animationParameters.apply {
            this.count = count
            this.index = index
            columnsCount = columns
            rowsCount = count / columns
            column = columns - 1 - (invertedIndex % columns)
            row = rowsCount - 1 - invertedIndex / columns
        }
        Log.d("GRID", "invertedIndex $invertedIndex inverted % columns ${(invertedIndex % columns)}")
        Log.d("GRID", "count ${animationParameters.count} - index ${animationParameters.index} - columns ${animationParameters.columnsCount} - rows ${animationParameters.rowsCount} - column ${animationParameters.column} - row ${animationParameters.row}")

        params?.layoutAnimationParameters = animationParameters
    }
}