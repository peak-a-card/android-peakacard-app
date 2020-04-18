package com.peakacard.host.voting.infrastructure.di

import com.peakacard.host.voting.view.CreateVotingViewModel
import org.koin.dsl.module

val createVotingModule = module {
    factory { CreateVotingViewModel() }
}
