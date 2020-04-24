package com.peakacard.app.infrastructure.di

import com.peakacard.app.common.navigator.AppNavigator
import org.koin.dsl.module

val appModule = module {
  factory { AppNavigator() }
}
