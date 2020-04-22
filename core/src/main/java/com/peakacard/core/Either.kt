package com.peakacard.core

sealed class Either<out L, out R> {
  data class Left<L>(val item: L) : Either<L, Nothing>()
  data class Right<R>(val item: R) : Either<Nothing, R>()

  fun <T> fold(left: (L) -> T, right: (R) -> T): T = when (this) {
    is Left -> left(item)
    is Right -> right(item)
  }
}
