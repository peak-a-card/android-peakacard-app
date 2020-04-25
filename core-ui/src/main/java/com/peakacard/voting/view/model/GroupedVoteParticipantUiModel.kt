package com.peakacard.voting.view.model

import com.peakacard.card.view.model.CardUiModel

class GroupedVoteParticipantUiModel(val voteResultCard: VoteResultCard, val participants: List<String>)

sealed class VoteResultCard(open val card: CardUiModel) {
  class Mode(override val card: CardUiModel): VoteResultCard(card)
  class Regular(override val card: CardUiModel): VoteResultCard(card)
}
