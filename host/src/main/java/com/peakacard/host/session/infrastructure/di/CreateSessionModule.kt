package com.peakacard.host.session.infrastructure.di

import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.CreateSessionViewModel
import com.peakacard.user.view.model.mapper.FirebaseUserMapper
import org.koin.dsl.module

val createSessionModule = module {
    factory { CreateSessionViewModel(get()) }
    factory { CreateSessionUseCase(get()) }
    factory { FirebaseUserMapper() }
}
