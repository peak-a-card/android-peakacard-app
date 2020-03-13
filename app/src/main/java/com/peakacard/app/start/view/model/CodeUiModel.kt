package com.peakacard.app.start.view.model

inline class CodeUiModel(val value: String) {
    fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}