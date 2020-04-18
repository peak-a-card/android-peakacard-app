package com.peakacard.host.session.infrastructure.di

import com.peakacard.host.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.host.session.data.repository.SessionRepository
import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.CreateSessionViewModel
import com.peakacard.session.view.model.mapper.FirebaseUserMapper
import org.koin.dsl.module

val createSessionModule = module {
    factory { CreateSessionViewModel(get()) }
    factory { CreateSessionUseCase(get()) }
    factory { SessionRepository(get()) }
    factory { SessionRemoteDataSource(get()) }
    factory { FirebaseUserMapper() }
}
