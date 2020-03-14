package com.peakacard.app.infrastructure.di

import com.peakacard.app.session.data.datasource.JoinSessionRemoteDatasource
import com.peakacard.app.session.data.repository.JoinSessionRepository
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.view.JoinSessionViewModel
import org.koin.dsl.module

val joinSessionModule = module {
    factory { JoinSessionViewModel(get()) }
    factory { JoinSessionUseCase(get()) }
    factory { JoinSessionRepository(get()) }
    factory { JoinSessionRemoteDatasource(get()) }
}