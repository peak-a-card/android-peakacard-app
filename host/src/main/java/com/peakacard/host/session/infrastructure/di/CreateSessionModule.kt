package com.peakacard.host.session.infrastructure.di

import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.CreateSessionViewModel
import org.koin.dsl.module

val createSessionModule = module {
  factory { CreateSessionViewModel(get()) }
  factory { CreateSessionUseCase(get()) }
}
