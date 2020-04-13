package com.peakacard.app.cards.infrastructure.di

import com.peakacard.app.cards.data.datasource.InMemoryCardsDataSource
import com.peakacard.app.cards.data.repository.CardsRepository
import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.CardsViewModel
import org.koin.dsl.module

val cardsModule = module {
    factory { CardsViewModel(get()) }
    factory { GetCardsUseCase(get()) }
    factory { CardsRepository(get()) }
    factory { InMemoryCardsDataSource() }
}
