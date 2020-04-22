package com.peakacard.host.voting.infrastructure.di

import com.peakacard.host.voting.domain.CreateVotingUseCase
import com.peakacard.host.voting.view.CreateVotingViewModel
import org.koin.dsl.module

val createVotingModule = module {
  factory { CreateVotingViewModel(get()) }
  factory { CreateVotingUseCase(get(), get()) }
}
