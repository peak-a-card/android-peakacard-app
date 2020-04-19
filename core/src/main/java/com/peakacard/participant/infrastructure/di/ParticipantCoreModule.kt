package com.peakacard.participant.infrastructure.di

import com.peakacard.participant.data.datasource.remote.ParticipantRemoteDataSource
import com.peakacard.participant.data.repository.ParticipantRepository
import org.koin.dsl.module

val participantCoreModule = module {
    factory { ParticipantRepository(get()) }
    factory { ParticipantRemoteDataSource(get()) }
}
