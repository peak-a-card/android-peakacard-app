package com.peakacard.app.recap.infrastructure.di

import com.peakacard.app.recap.view.RecapViewModel
import org.koin.dsl.module

val recapModule = module {
  factory { RecapViewModel(get(), get(), get()) }
}
