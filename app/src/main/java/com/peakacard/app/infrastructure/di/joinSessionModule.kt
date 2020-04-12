package com.peakacard.app.infrastructure.di

import android.content.Context
import com.peakacard.app.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.app.session.data.datasource.remote.SessionRemoteDatasource
import com.peakacard.app.session.data.model.mapper.UserMapper
import com.peakacard.app.session.data.repository.SessionRepository
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.view.JoinSessionViewModel
import com.peakacard.app.session.view.model.mapper.FirebaseUserMapper
import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val joinSessionModule = module {
    factory { JoinSessionViewModel(get(), get()) }
    factory { JoinSessionUseCase(get()) }
    factory { SessionRepository(get(), get(), get()) }
    factory {
        SessionRemoteDatasource(
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

    factory { FirebaseUserMapper() }
    factory { UserUiModelMapper() }
    factory { UserMapper() }
}

private const val SESSION_SHARED_PREFERENCES = "SESSION_SHARED_PREFERENCES"
