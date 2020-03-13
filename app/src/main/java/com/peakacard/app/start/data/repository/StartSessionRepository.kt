package com.peakacard.app.start.data.repository

import com.peakacard.app.start.data.datasource.StartSessionRemoteDatasource
import com.peakacard.app.start.domain.model.SessionDomainModel

class StartSessionRepository(private val remoteDatasource: StartSessionRemoteDatasource) {
    fun startSession(session: SessionDomainModel) {
        remoteDatasource.startSession(session.name.value, session.code.value)
    }
}