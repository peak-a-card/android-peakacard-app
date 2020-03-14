package com.peakacard.app.session.view.model

inline class NameUiModel(val value: String) {
    fun isEmpty(): Boolean {
        return value.isEmpty()
    }
}