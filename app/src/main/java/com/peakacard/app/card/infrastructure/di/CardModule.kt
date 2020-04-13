package com.peakacard.app.card.infrastructure.di

import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import org.koin.dsl.module

val cardModule = module {
    factory { CardUiModelMapper() }
}
