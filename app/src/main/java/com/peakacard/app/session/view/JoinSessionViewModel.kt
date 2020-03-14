package com.peakacard.app.session.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.domain.model.ParticipantName
import com.peakacard.app.session.domain.model.SessionCode
import com.peakacard.app.session.domain.model.JoinSessionRequest
import com.peakacard.app.session.domain.model.JoinSessionResponse
import com.peakacard.app.session.view.model.CodeUiModel
import com.peakacard.app.session.view.model.NameUiModel
import com.peakacard.app.session.view.state.JoinSessionState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class JoinSessionViewModel(private val joinSessionUseCase: JoinSessionUseCase) : ViewModel() {

    private val joinSessionState: BroadcastChannel<JoinSessionState> = ConflatedBroadcastChannel()

    fun bindView(view: JoinSessionView) {
        viewModelScope.launch {
            joinSessionState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun joinSession(name: NameUiModel, code: CodeUiModel) {
        if (name.isEmpty()) {
            joinSessionState.offer(JoinSessionState.Error.NameRequiredError)
            return
        }
        if (code.isEmpty()) {
            joinSessionState.offer(JoinSessionState.Error.CodeRequiredError)
            return
        }
        joinSessionState.offer(JoinSessionState.JoiningSession)
        viewModelScope.launch {
            delay(2000)
            joinSessionUseCase.joinSession(
                JoinSessionRequest(
                    ParticipantName(name.value),
                    SessionCode(code.value)
                )
            ).fold(
                {
                    when (it) {
                        JoinSessionResponse.Error.NoSessionFound -> {
                            joinSessionState.offer(JoinSessionState.Error.NoSessionFound)
                        }
                        JoinSessionResponse.Error.Unspecified -> {
                            joinSessionState.offer(JoinSessionState.Error.Unspecified)
                        }
                    }
                },
                { joinSessionState.offer(JoinSessionState.Joined) }
            )
        }
    }
}