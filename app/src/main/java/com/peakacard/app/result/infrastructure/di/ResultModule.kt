package com.peakacard.app.result.infrastructure.di

import com.peakacard.app.result.domain.GetFinalVotingResultUseCase
import com.peakacard.app.result.domain.GetVotingResultUseCase
import com.peakacard.app.result.view.VotingResultViewModel
import org.koin.dsl.module

val resultModule = module {
    factory { VotingResultViewModel(get(), get(), get()) }
    factory { GetVotingResultUseCase(get(), get(), get()) }
    factory { GetFinalVotingResultUseCase(get(), get(), get()) }
}
