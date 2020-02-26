package com.peakacard.core.ui.extensions

import android.app.Activity
import android.view.View
import com.peakacard.core.ui.extensions.internal.ViewFinder
import com.peakacard.core.ui.extensions.internal.required
import kotlin.properties.ReadOnlyProperty

fun <V : View> Activity.bindView(id: Int)
        : ReadOnlyProperty<Activity, V> =
    required(id, viewFinder)

private val Activity.viewFinder: ViewFinder<Activity>
    get() = { findViewById(it) }