package com.peakacard.user.infrastructure.di

import android.content.Context
import com.peakacard.session.data.datasource.local.SessionLocalDataSource
import com.peakacard.session.data.datasource.remote.SessionRemoteDataSource
import com.peakacard.session.data.repository.SessionRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val sessionCoreModule = module {
  factory { SessionRepository(get(), get(), get()) }
  factory { SessionRemoteDataSource(get()) }
  factory { SessionLocalDataSource(get(named(SESSION_SHARED_PREFERENCES))) }
  factory(named(SESSION_SHARED_PREFERENCES)) {
    androidContext().getSharedPreferences(
      SESSION_SHARED_PREFERENCES,
      Context.MODE_PRIVATE
    )
  }
}

private const val SESSION_SHARED_PREFERENCES = "SESSION_SHARED_PREFERENCES"
