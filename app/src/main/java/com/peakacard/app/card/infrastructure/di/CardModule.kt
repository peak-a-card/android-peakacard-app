package com.peakacard.app.card.infrastructure.di

import com.peakacard.app.card.domain.SendVoteUseCase
import com.peakacard.app.card.view.CardViewModel
import com.peakacard.app.card.view.model.mapper.CardModelMapper
import org.koin.dsl.module

val cardModule = module {
    factory { CardViewModel(get(), get()) }
    factory { SendVoteUseCase(get(), get(), get(), get()) }
    factory { CardModelMapper() }
}
