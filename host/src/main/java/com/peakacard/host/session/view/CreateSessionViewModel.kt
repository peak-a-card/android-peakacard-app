package com.peakacard.host.session.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.state.CreateSessionState
import com.peakacard.session.view.model.UserUiModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSessionViewModel(private val createSessionUseCase: CreateSessionUseCase) :
    PeakViewModel<CreateSessionState>() {

    fun createSession(user: UserUiModel?) {
        if (user == null) {
            state.offer(CreateSessionState.Error.UserSignIn)
            return
        }
        state.offer(CreateSessionState.GeneratingSessionId)
        generateSessionCode()
    }

    private fun generateSessionCode() {
        viewModelScope.launch {
            createSessionUseCase.createSession().fold(
                {
                    Timber.e("Error creating session id")
                    state.offer(CreateSessionState.Error.CannotGenerateId)
                },
                {
                    Timber.d("Session id created: ${it.id}")
                    state.offer(CreateSessionState.SessionIdGenerated(it.id.toString()))
                }
            )
        }
    }
}
