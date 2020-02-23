package com.peakacard.app.infrastructure.di

import com.peakacard.app.data.datasource.InMemoryCardsDataSource
import com.peakacard.app.data.repository.CardsRepository
import com.peakacard.app.domain.GetCardsUseCase
import com.peakacard.app.view.CardsViewModel
import org.koin.dsl.module

val appModule = module {
    factory { CardsViewModel(get()) }
    factory { GetCardsUseCase(get()) }
    factory { CardsRepository(get()) }
    factory { InMemoryCardsDataSource() }
}