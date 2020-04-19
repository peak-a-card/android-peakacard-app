package com.peakacard.core.infrastructure.di

import com.peakacard.card.infrastructure.di.cardCoreUiModule
import com.peakacard.user.infrastructure.di.userCoreUiModule

val coreUiModules = listOf(
    userCoreUiModule,
    cardCoreUiModule
)
