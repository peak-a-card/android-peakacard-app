package com.peakacard.app.session.view

import androidx.lifecycle.viewModelScope
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import com.peakacard.app.session.view.state.JoinSessionState
import com.peakacard.core.view.PeakViewModel
import com.peakacard.session.domain.model.JoinSessionResponse
import com.peakacard.session.domain.model.UserSession
import com.peakacard.user.view.model.UserUiModel
import kotlinx.coroutines.launch

class JoinSessionViewModel(
  private val joinSessionUseCase: JoinSessionUseCase,
  private val userUiModelMapper: UserUiModelMapper
) : PeakViewModel<JoinSessionState>() {

  fun joinSession(user: UserUiModel?, code: String) {
    if (user == null) {
      state.offer(JoinSessionState.Error.UserSignInError)
      return
    }
    if (code.isEmpty()) {
      state.offer(JoinSessionState.Error.CodeRequiredError)
      return
    }
    state.offer(JoinSessionState.JoiningSession)
    viewModelScope.launch {
      joinSessionUseCase.joinSession(UserSession(userUiModelMapper.map(user), code))
        .fold(
          {
            when (it) {
              JoinSessionResponse.Error.NoSessionFound -> {
                state.offer(JoinSessionState.Error.NoSessionFound)
              }
              JoinSessionResponse.Error.Unspecified -> {
                state.offer(JoinSessionState.Error.Unspecified)
              }
            }
          },
          { state.offer(JoinSessionState.Joined) }
        )
    }
  }
}
