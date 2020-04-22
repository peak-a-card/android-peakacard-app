package com.peakacard.core.infrastructure.di

import com.peakacard.card.infrastructure.di.cardCoreModule
import com.peakacard.cards.infrastructure.di.cardsCoreModule
import com.peakacard.participant.infrastructure.di.participantCoreModule
import com.peakacard.user.infrastructure.di.sessionCoreModule
import com.peakacard.user.infrastructure.di.userCoreModule
import com.peakacard.voting.infrastructure.di.votingCoreModule

val coreModules = listOf(
  cardCoreModule,
  cardsCoreModule,
  participantCoreModule,
  sessionCoreModule,
  userCoreModule,
  votingCoreModule,
  dataModule
)
