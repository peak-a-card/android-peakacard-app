package com.peakacard.cards.infrastructure.di

import com.peakacard.cards.data.datasource.local.CardsLocalDataSource
import com.peakacard.cards.data.repository.CardsRepository
import org.koin.dsl.module

val cardsCoreModule = module {
  factory { CardsRepository(get()) }
  factory { CardsLocalDataSource() }
}
