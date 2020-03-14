package com.peakacard.app.session.view.model

inline class CodeUiModel(val value: String) {
    fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}