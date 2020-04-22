package com.peakacard.core.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

abstract class PeakViewModel<S> : ViewModel() {

  val state: BroadcastChannel<S> = ConflatedBroadcastChannel()

  open fun bindView(view: PeakView<S>) {
    viewModelScope.launch {
      state
        .asFlow()
        .collect { view.updateState(it) }
    }
  }
}
