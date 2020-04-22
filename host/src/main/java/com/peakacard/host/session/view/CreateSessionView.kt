package com.peakacard.host.session.view

import com.peakacard.core.view.PeakView
import com.peakacard.host.session.view.state.CreateSessionState

interface CreateSessionView : PeakView<CreateSessionState> {
  fun configureToCreateSession()
  fun configureToCreateVote()
}
