package com.peakacard.host.session.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.session.domain.GenerateSessionCodeUseCase
import com.peakacard.host.session.view.state.CreateSessionState
import com.peakacard.session.view.model.UserUiModel
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSessionViewModel(private val generateSessionCodeUseCase: GenerateSessionCodeUseCase) :
    PeakViewModel<CreateSessionState>() {

    override val state: BroadcastChannel<CreateSessionState> = ConflatedBroadcastChannel()

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
            generateSessionCodeUseCase.generateSessionCode().fold(
                {
                    Timber.e("Error generating session id")
                    state.offer(CreateSessionState.Error.CannotGenerateId)
                },
                {
                    Timber.d("Session id generated: ${it.id}")
//                    createSessionById(it.id)
                    state.offer(CreateSessionState.SessionIdGenerated(it.id.toString()))
                }
            )
        }
    }

    private fun createSessionById(sessionId: Int) {
        viewModelScope.launch {
            // state.offer(CreateSessionState.SessionIdGenerated(it.id.toString()))
        }
    }
}
