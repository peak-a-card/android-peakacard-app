package com.peakacard.app.start.view.model

inline class CodeUiModel(private val value: String) {
    fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}