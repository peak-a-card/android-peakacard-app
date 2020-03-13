package com.peakacard.app.start.view

import com.peakacard.app.start.view.state.StartSessionState

interface StartSessionView {
    fun updateState(state: StartSessionState)
}