package com.peakacard.app.infrastructure.di

import com.peakacard.app.session.data.datasource.JoinSessionRemoteDatasource
import com.peakacard.app.session.data.model.mapper.UserMapper
import com.peakacard.app.session.data.repository.JoinSessionRepository
import com.peakacard.app.session.domain.JoinSessionUseCase
import com.peakacard.app.session.view.JoinSessionViewModel
import com.peakacard.app.session.view.model.mapper.FirebaseUserMapper
import com.peakacard.app.session.view.model.mapper.UserUiModelMapper
import org.koin.dsl.module

val joinSessionModule = module {
    factory { JoinSessionViewModel(get(), get()) }
    factory { JoinSessionUseCase(get()) }
    factory { JoinSessionRepository(get(), get()) }
    factory { JoinSessionRemoteDatasource(get()) }

    factory { FirebaseUserMapper() }
    factory { UserUiModelMapper() }
    factory { UserMapper() }
}
