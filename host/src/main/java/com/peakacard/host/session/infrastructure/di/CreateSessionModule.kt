package com.peakacard.host.session.infrastructure.di

import android.content.Context
import com.peakacard.host.session.domain.CreateSessionUseCase
import com.peakacard.host.session.view.CreateSessionViewModel
import com.peakacard.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.session.data.repository.SessionRepository
import com.peakacard.session.view.model.mapper.FirebaseUserMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val createSessionModule = module {
    factory { CreateSessionViewModel(get()) }
    factory { CreateSessionUseCase(get()) }
    factory { SessionRepository(get(), get(), get()) }
    factory { SessionRemoteDataSource(get()) }
    factory { SessionLocalDataSource(get(named(SESSION_SHARED_PREFERENCES))) }
    factory(named(SESSION_SHARED_PREFERENCES)) {
        androidContext().getSharedPreferences(
            SESSION_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
    factory { FirebaseUserMapper() }
}

private const val SESSION_SHARED_PREFERENCES = "SESSION_SHARED_PREFERENCES"
