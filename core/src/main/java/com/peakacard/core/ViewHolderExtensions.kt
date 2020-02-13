package com.peakacard.core

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peakacard.core.internal.ViewFinder
import com.peakacard.core.internal.required
import kotlin.properties.ReadOnlyProperty

fun <V : View> RecyclerView.ViewHolder.bindView(id: Int)
        : ReadOnlyProperty<RecyclerView.ViewHolder, V> = required(id, viewFinder)

private val RecyclerView.ViewHolder.viewFinder: ViewFinder<RecyclerView.ViewHolder>
    get() = { itemView.findViewById(it) }