package com.peakacard.app.session.view

import com.peakacard.app.session.view.state.JoinSessionState

interface JoinSessionView {
    fun updateState(state: JoinSessionState)
}