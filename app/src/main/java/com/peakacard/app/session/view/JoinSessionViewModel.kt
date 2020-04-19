package com.peakacard.app.session.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.session.domain.model.UserSession
import com.peakacard.session.domain.model.JoinSessionResponse
import com.peakacard.user.view.model.UserUiModel
import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import com.peakacard.app.session.view.state.JoinSessionState
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class JoinSessionViewModel(
    private val joinSessionUseCase: JoinSessionUseCase,
    private val userUiModelMapper: UserUiModelMapper
) : ViewModel() {

    private val joinSessionState: BroadcastChannel<JoinSessionState> = ConflatedBroadcastChannel()

    fun bindView(view: JoinSessionView) {
        viewModelScope.launch {
            joinSessionState
                .asFlow()
                .collect { view.updateState(it) }
        }
    }

    fun joinSession(user: UserUiModel?, code: String) {
        if (user == null) {
            joinSessionState.offer(JoinSessionState.Error.UserSignInError)
            return
        }
        if (code.isEmpty()) {
            joinSessionState.offer(JoinSessionState.Error.CodeRequiredError)
            return
        }
        joinSessionState.offer(JoinSessionState.JoiningSession)
        viewModelScope.launch {
            joinSessionUseCase.joinSession(UserSession(userUiModelMapper.map(user), code))
                .fold(
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
