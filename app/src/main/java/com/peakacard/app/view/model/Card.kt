package com.peakacard.app.view.model

import com.peakacard.app.CardDisplay

enum class Card(val display: CardDisplay) {
    ZERO("0"),
    HALF("1/2"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FIVE("5"),
    EIGHT("8"),
    THIRTEEN("13"),
    TWENTY("20"),
    FORTY("40"),
    HUNDRED("100"),
    INFINITE("&#8734;"),
    UNKNOWN("?"),
    COFFEE("\u2615");

    companion object {
        fun fromScore(score: Float): Card {
            return when (score) {
                0f -> ZERO
                1f -> ONE
                2f -> TWO
                3f -> THREE
                5f -> FIVE
                8f -> EIGHT
                13f -> THIRTEEN
                20f -> TWENTY
                40f -> FORTY
                100f -> HUNDRED
                999f -> INFINITE
                -1f -> UNKNOWN
                -2f -> COFFEE
                else -> UNKNOWN
            }
        }
    }
}