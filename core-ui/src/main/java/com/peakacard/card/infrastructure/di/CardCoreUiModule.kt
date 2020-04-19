package com.peakacard.card.infrastructure.di

import com.peakacard.card.view.model.mapper.CardUiModelMapper
import org.koin.dsl.module

val cardCoreUiModule = module {
    factory { CardUiModelMapper() }
}
