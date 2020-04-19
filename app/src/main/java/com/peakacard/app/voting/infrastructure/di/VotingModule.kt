package com.peakacard.app.voting.infrastructure.di

import com.peakacard.app.voting.domain.GetEndedVotingUseCase
import com.peakacard.app.voting.domain.GetStartedVotingUseCase
import com.peakacard.app.voting.view.WaitVotingViewModel
import org.koin.dsl.module

val votingModule = module {
    factory { WaitVotingViewModel(get(), get(), get()) }
    factory { GetStartedVotingUseCase(get(), get()) }
    factory { GetEndedVotingUseCase(get(), get()) }

}
