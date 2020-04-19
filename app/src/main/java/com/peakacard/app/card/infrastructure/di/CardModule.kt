package com.peakacard.app.card.infrastructure.di

import com.peakacard.card.data.datasource.remote.CardRemoteDataSource
import com.peakacard.card.data.datasource.remote.model.mapper.VoteMapper
import com.peakacard.card.data.repository.CardRepository
import com.peakacard.app.card.domain.SendVoteUseCase
import com.peakacard.app.card.view.CardViewModel
import com.peakacard.app.card.view.model.mapper.CardModelMapper
import com.peakacard.app.card.view.model.mapper.CardUiModelMapper
import org.koin.dsl.module

val cardModule = module {
    factory { CardViewModel(get(), get()) }
    factory { SendVoteUseCase(get(), get(), get(), get()) }
    factory { CardModelMapper() }
    factory { CardRepository(get(), get()) }
    factory { CardRemoteDataSource(get()) }
    factory { VoteMapper() }
    factory { CardUiModelMapper() }
}
