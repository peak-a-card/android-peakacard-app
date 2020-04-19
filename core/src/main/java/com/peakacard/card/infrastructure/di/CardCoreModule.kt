package com.peakacard.card.infrastructure.di

import com.peakacard.card.data.datasource.remote.CardRemoteDataSource
import com.peakacard.card.data.datasource.remote.model.mapper.VoteMapper
import com.peakacard.card.data.repository.CardRepository
import org.koin.dsl.module

val cardCoreModule = module {
    factory { CardRepository(get(), get()) }
    factory { CardRemoteDataSource(get()) }
    factory { VoteMapper() }
}
