package com.peakacard.app.start.domain

import com.peakacard.app.start.data.repository.StartSessionRepository
import com.peakacard.app.start.domain.model.SessionDomainModel

class StartSessionUseCase(private val repository: StartSessionRepository) {
    fun startSession(session: SessionDomainModel) {
        repository.startSession(session)
    }
}