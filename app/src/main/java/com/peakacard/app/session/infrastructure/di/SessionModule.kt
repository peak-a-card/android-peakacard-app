package com.peakacard.app.session.infrastructure.di

import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.app.session.view.JoinSessionViewModel
import org.koin.dsl.module

val sessionModule = module {
    factory { JoinSessionViewModel(get(), get()) }
    factory { JoinSessionUseCase(get(), get()) }
    factory { LeaveSessionUseCase(get(), get()) }
}
