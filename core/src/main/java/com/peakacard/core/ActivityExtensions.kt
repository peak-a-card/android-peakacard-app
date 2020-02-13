package com.peakacard.core

import android.app.Activity
import android.view.View
import com.peakacard.core.internal.ViewFinder
import com.peakacard.core.internal.required
import kotlin.properties.ReadOnlyProperty

fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> =
    required(id, viewFinder)

private val Activity.viewFinder: ViewFinder<Activity>
    get() = { findViewById(it) }