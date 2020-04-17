package com.peakacard.host.session.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.session.domain.GenerateSessionCodeUseCase
import com.peakacard.host.session.view.state.CreateSessionState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSessionViewModel(private val generateSessionCodeUseCase: GenerateSessionCodeUseCase) :
    PeakViewModel<CreateSessionState>() {

  override val state: BroadcastChannel<CreateSessionState> = ConflatedBroadcastChannel()

  fun generateSessionCode() {
    viewModelScope.launch {
      generateSessionCodeUseCase.generateSessionCode().fold(
          {
            Timber.e("Error generating session id")
            state.offer(CreateSessionState.Error)
          },
          {
            Timber.d("Session id generated: ${it.id}")
            state.offer(CreateSessionState.SessionIdGenerated(it.id.toString()))
          }
      )
    }
  }
}
