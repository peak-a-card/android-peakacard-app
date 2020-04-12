package com.peakacard.app.voting.infrastructure.di

import com.peakacard.app.voting.data.datasource.remote.VotingRemoteDataSource
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.GetVotingUseCase
import com.peakacard.app.voting.view.WaitVotingViewModel
import org.koin.dsl.module

val votingModule = module {
    factory { WaitVotingViewModel(get(), get()) }
    factory { GetVotingUseCase(get(), get()) }
    factory { VotingRepository(get()) }
    factory { VotingRemoteDataSource(get()) }
}
