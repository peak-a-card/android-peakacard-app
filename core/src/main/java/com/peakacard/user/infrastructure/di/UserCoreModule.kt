package com.peakacard.user.infrastructure.di

import android.content.Context
import com.peakacard.session.data.datasource.remote.model.mapper.UserDataModelMapper
import com.peakacard.session.data.datasource.remote.model.mapper.UserMapper
import com.peakacard.user.data.datasource.local.UserLocalDataSource
import com.peakacard.user.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val userCoreModule = module {
  factory { UserRepository(get(), get(), get()) }
  factory { UserLocalDataSource(get(named(USER_SHARED_PREFERENCES)), get()) }
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
