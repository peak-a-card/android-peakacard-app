package com.peakacard.host.voting.infrastructure.di

import com.peakacard.host.voting.domain.EndVoteUseCase
import com.peakacard.host.voting.domain.GetParticipantsVoteUseCase
import com.peakacard.host.voting.view.WaitingVotesViewModel
import org.koin.dsl.module

val waitingVotesModule = module {
  factory { WaitingVotesViewModel(get(), get()) }
  factory { GetParticipantsVoteUseCase(get(), get(), get()) }
  factory { EndVoteUseCase(get(), get()) }
}
