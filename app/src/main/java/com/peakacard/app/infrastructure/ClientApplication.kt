package com.peakacard.app.infrastructure

import com.peakacard.app.card.infrastructure.di.cardModule
import com.peakacard.app.cards.infrastructure.di.cardsModule
import com.peakacard.app.participant.infrastructure.di.participantModule
import com.peakacard.app.recap.infrastructure.di.recapModule
import com.peakacard.app.result.infrastructure.di.resultModule
import com.peakacard.app.session.infrastructure.di.sessionModule
import com.peakacard.app.user.infrastructure.di.userModule
import com.peakacard.app.voting.infrastructure.di.votingModule
import com.peakacard.core.infrastructure.PeakACardApplication
import org.koin.core.module.Module

class ClientApplication : PeakACardApplication() {
  override val modules: List<Module> = listOf(
    cardsModule,
    cardModule,
    sessionModule,
    votingModule,
    participantModule,
    userModule,
    resultModule,
    recapModule
  )
}
