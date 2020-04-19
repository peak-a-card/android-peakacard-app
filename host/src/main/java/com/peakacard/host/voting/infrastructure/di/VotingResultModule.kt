package com.peakacard.host.voting.infrastructure.di

import com.peakacard.host.voting.view.VotingResultViewModel
import org.koin.dsl.module

val votingResultModule = module {
    factory { VotingResultViewModel(get(), get()) }
}
