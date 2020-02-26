package com.peakacard.core.ui.extensions

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.ui.extensions.internal.ViewFinder
import com.peakacard.core.ui.extensions.internal.required
import kotlin.properties.ReadOnlyProperty

fun <V : View> RecyclerView.ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V> =
    required(id, viewFinder)

private val RecyclerView.ViewHolder.viewFinder: ViewFinder<RecyclerView.ViewHolder>
    get() = { itemView.findViewById(it) }