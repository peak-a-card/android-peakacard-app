package com.peakacard.user.infrastructure.di

import com.peakacard.user.view.model.mapper.FirebaseUserMapper
import org.koin.dsl.module

val userCoreUiModule = module {
    factory { FirebaseUserMapper() }
}
