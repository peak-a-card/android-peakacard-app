package com.peakacard.app.card.view.model.mapper

import com.peakacard.card.domain.model.Card
import com.peakacard.card.view.model.CardUiModel

class CardModelMapper {

    fun map(cardUiModel: CardUiModel): Card {
        return Card(
            when (cardUiModel) {
                CardUiModel.ZERO -> 0f
                CardUiModel.HALF -> 0.5f
                CardUiModel.ONE -> 1f
                CardUiModel.TWO -> 2f
                CardUiModel.THREE -> 3f
                CardUiModel.FIVE -> 5f
                CardUiModel.EIGHT -> 8f
                CardUiModel.THIRTEEN -> 13f
                CardUiModel.TWENTY -> 20f
                CardUiModel.FORTY -> 40f
                CardUiModel.HUNDRED -> 100f
                CardUiModel.INFINITE -> 999f
                CardUiModel.UNKNOWN -> -1f
                CardUiModel.COFFEE -> -2f
            }
        )
    }
}
