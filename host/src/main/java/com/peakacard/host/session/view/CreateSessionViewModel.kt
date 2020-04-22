package com.peakacard.host.session.view

import androidx.lifecycle.viewModelScope
import com.peakacard.core.view.PeakView
import com.peakacard.core.view.PeakViewModel
import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.state.CreateSessionState
import com.peakacard.user.view.model.UserUiModel
import kotlinx.coroutines.launch
import timber.log.Timber

class CreateSessionViewModel(private val createSessionUseCase: CreateSessionUseCase) :
  PeakViewModel<CreateSessionState>() {

  private var status: Status = Status.INIT
  private lateinit var createSessionView: CreateSessionView

  override fun bindView(view: PeakView<CreateSessionState>) {
    super.bindView(view)
    createSessionView = view as CreateSessionView
  }

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
          status = Status.SESSION_CREATED
          state.offer(CreateSessionState.SessionIdGenerated(it.id))
        }
      )
    }
  }

  fun initView() {
    when (status) {
      Status.INIT -> createSessionView.configureToCreateSession()
      Status.SESSION_CREATED -> createSessionView.configureToCreateVote()
    }
  }

  private enum class Status {
    INIT,
    SESSION_CREATED
  }
}
