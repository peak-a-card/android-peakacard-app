package com.peakacard.app.start.view.model

inline class NameUiModel(private val value: String) {
    fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}