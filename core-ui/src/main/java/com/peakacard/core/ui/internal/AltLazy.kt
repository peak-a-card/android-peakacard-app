package com.peakacard.core.ui.internal

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
internal class Lazy<T, V>(private val initializer: (T, KProperty<*>) -> V) :
    ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }
        @Suppress("UNCHECKED_CAST")
        return value as V
    }
}