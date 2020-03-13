package com.peakacard.app.infrastructure.di

import com.peakacard.app.start.domain.StartSessionUseCase
import com.peakacard.app.start.view.StartSessionViewModel
import org.koin.dsl.module

val startSessionModule = module {
    factory { StartSessionViewModel(get()) }
    factory { StartSessionUseCase() }
}