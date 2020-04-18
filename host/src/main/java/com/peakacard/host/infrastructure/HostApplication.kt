package com.peakacard.host.infrastructure

import com.peakacard.core.infrastructure.PeakACardApplication
import com.peakacard.host.infrastructure.di.dataModule
import com.peakacard.host.session.infrastructure.di.createSessionModule
import com.peakacard.host.voting.infrastructure.di.createVotingModule
import org.koin.core.module.Module

class HostApplication : PeakACardApplication() {
    override val modules: List<Module> = listOf(
        dataModule,
        createSessionModule,
        createVotingModule
    )
}
