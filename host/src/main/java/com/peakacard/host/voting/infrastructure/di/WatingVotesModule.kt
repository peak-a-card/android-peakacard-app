package com.peakacard.host.voting.infrastructure.di

import com.peakacard.host.voting.view.WaitingVotesViewModel
import org.koin.dsl.module

val waitingVotesModule = module {
    factory { WaitingVotesViewModel() }
}
