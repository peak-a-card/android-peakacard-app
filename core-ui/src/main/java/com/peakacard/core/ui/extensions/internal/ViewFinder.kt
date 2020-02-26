package com.peakacard.core.ui.extensions.internal

import android.view.View
import kotlin.reflect.KProperty

internal fun viewNotFound(id: Int, desc: KProperty<*>): Nothing =
    throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
internal fun <T, V : View> required(id: Int, finder: ViewFinder<T>) =
    Lazy { t: T, desc ->
        t.finder(id) as V?
            ?: viewNotFound(
                id,
                desc
            )
    }

typealias ViewFinder<T> = T.(Int) -> View?