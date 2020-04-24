package com.peakacard.host.infrastructure

import com.peakacard.core.infrastructure.PeakACardApplication
import com.peakacard.core.infrastructure.di.dataModule
import com.peakacard.host.infrastructure.di.hostModule
import com.peakacard.host.session.infrastructure.di.createSessionModule
import com.peakacard.host.voting.infrastructure.di.createVotingModule
import com.peakacard.host.voting.infrastructure.di.votingResultModule
import com.peakacard.host.voting.infrastructure.di.waitingVotesModule
import org.koin.core.module.Module

class HostApplication : PeakACardApplication() {
  override val modules: List<Module> = listOf(
    hostModule,
    dataModule,
    createSessionModule,
    createVotingModule,
    waitingVotesModule,
    votingResultModule
  )
}
