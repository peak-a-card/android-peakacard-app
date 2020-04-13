package com.peakacard.app.session.infrastructure.di

import android.content.Context
import com.peakacard.app.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.app.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.domain.LeaveSessionUseCase
import com.peakacard.app.session.view.JoinSessionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sessionModule = module {
    factory { JoinSessionViewModel(get(), get()) }
    factory { JoinSessionUseCase(get(), get()) }
    factory { SessionRepository(get(), get(), get()) }
    factory {
        SessionRemoteDataSource(
            get()
        )
    }
    factory { SessionLocalDataSource(get(named(SESSION_SHARED_PREFERENCES))) }
    factory(named(SESSION_SHARED_PREFERENCES)) {
        androidContext().getSharedPreferences(
            SESSION_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

    factory { LeaveSessionUseCase(get(), get()) }
}

private const val SESSION_SHARED_PREFERENCES = "SESSION_SHARED_PREFERENCES"
