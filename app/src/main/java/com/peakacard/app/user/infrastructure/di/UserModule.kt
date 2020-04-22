package com.peakacard.app.user.infrastructure.di

import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import org.koin.dsl.module

val userModule = module {
  factory { UserUiModelMapper() }
}
