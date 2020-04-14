package com.peakacard.app.participant.infrastructure.di

import com.peakacard.app.participant.data.datasource.remote.ParticipantRemoteDataSource
import com.peakacard.app.participant.data.repository.ParticipantRepository
import com.peakacard.app.participant.domain.GetSessionParticipantUseCase
import org.koin.dsl.module

val participantModule = module {
    factory { GetSessionParticipantUseCase(get(), get()) }

    factory { ParticipantRepository(get()) }
    factory { ParticipantRemoteDataSource(get()) }
}
