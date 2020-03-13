package com.peakacard.app.start.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.start.domain.StartSessionUseCase
import com.peakacard.app.start.view.model.CodeUiModel
import com.peakacard.app.start.view.model.NameUiModel
import com.peakacard.app.start.view.state.StartSessionState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StartSessionViewModel(private val startSessionUseCase: StartSessionUseCase) : ViewModel() {

    private val startSessionState: BroadcastChannel<StartSessionState> = ConflatedBroadcastChannel()

    fun bindView(view: StartSessionView) {
        viewModelScope.launch {
            startSessionState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun startSession(name: NameUiModel, code: CodeUiModel) {
        if (name.isEmpty()) {
            startSessionState.offer(StartSessionState.Error.NameRequiredError)
            return
        }
        if (code.isEmpty()) {
            startSessionState.offer(StartSessionState.Error.CodeRequiredError)
            return
        }
        viewModelScope.launch {

        }
    }
}