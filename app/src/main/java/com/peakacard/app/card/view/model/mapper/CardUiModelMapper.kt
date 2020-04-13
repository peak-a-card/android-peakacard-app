package com.peakacard.app.card.view.model.mapper

import com.peakacard.app.card.domain.model.Card
import com.peakacard.app.card.view.model.CardUiModel

class CardUiModelMapper {
    fun map(cardDomainModel: Card): CardUiModel {
        return when (cardDomainModel.score) {
            0f -> CardUiModel.ZERO
            0.5f -> CardUiModel.HALF
            1f -> CardUiModel.ONE
            2f -> CardUiModel.TWO
            3f -> CardUiModel.THREE
            5f -> CardUiModel.FIVE
            8f -> CardUiModel.EIGHT
            13f -> CardUiModel.THIRTEEN
            20f -> CardUiModel.TWENTY
            40f -> CardUiModel.FORTY
            100f -> CardUiModel.HUNDRED
            999f -> CardUiModel.INFINITE
            -1f -> CardUiModel.UNKNOWN
            -2f -> CardUiModel.COFFEE
            else -> CardUiModel.UNKNOWN
        }
    }
}
