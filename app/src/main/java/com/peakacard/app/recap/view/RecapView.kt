package com.peakacard.app.recap.view

import com.peakacard.app.recap.view.state.RecapState

interface RecapView {
  fun updateState(state: RecapState)
}
