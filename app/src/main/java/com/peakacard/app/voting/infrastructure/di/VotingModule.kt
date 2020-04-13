package com.peakacard.app.voting.infrastructure.di

import android.content.Context
import com.peakacard.app.voting.data.datasource.local.VotingLocalDataSource
import com.peakacard.app.voting.data.datasource.remote.VotingRemoteDataSource
import com.peakacard.app.voting.data.repository.VotingRepository
import com.peakacard.app.voting.domain.GetVotingUseCase
import com.peakacard.app.voting.view.WaitVotingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

val votingModule = module {
    factory { WaitVotingViewModel(get(), get(), get(), get()) }
    factory { GetVotingUseCase(get(), get()) }
    factory { VotingRepository(get(), get()) }
    factory { VotingRemoteDataSource(get()) }
    factory { VotingLocalDataSource(get(named(VOTING_SHARED_PREFERENCES))) }
    factory(named(VOTING_SHARED_PREFERENCES)) {
        androidContext().getSharedPreferences(
            VOTING_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }
}

private const val VOTING_SHARED_PREFERENCES = "VOTING_SHARED_PREFERENCES"
