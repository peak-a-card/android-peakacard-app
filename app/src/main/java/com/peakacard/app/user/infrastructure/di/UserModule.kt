package com.peakacard.app.user.infrastructure.di

import android.content.Context
import com.peakacard.app.session.data.datasource.remote.model.mapper.UserDataModelMapper
import com.peakacard.app.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.session.view.model.mapper.FirebaseUserMapper
import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import com.peakacard.app.user.data.datasource.local.UserLocalDataSource
import com.peakacard.app.user.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userModule = module {
    factory { UserRepository(get(), get(), get()) }
    factory { UserLocalDataSource(get(named(USER_SHARED_PREFERENCES)), get()) }

    factory { FirebaseUserMapper() }
    factory { UserUiModelMapper() }
    factory { UserMapper() }
    factory { UserDataModelMapper() }

    factory(named(USER_SHARED_PREFERENCES)) {
        androidContext().getSharedPreferences(
            USER_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}

private const val USER_SHARED_PREFERENCES = "USER_SHARED_PREFERENCES"
