package com.peakacard.app.cards.infrastructure.di

import com.peakacard.app.cards.domain.GetCardsUseCase
import com.peakacard.app.cards.view.CardsViewModel
import org.koin.dsl.module

val cardsModule = module {
    factory { CardsViewModel(get(), get(), get()) }
    factory { GetCardsUseCase(get()) }
}
