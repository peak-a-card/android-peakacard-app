package com.peakacard.host.infrastructure.di

import com.peakacard.host.common.navigator.HostNavigator
import org.koin.dsl.module

val hostModule = module {
  factory { HostNavigator() }
}
