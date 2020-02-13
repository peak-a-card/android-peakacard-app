package com.peakacard.app

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
    COFFEE("\u2615")
}