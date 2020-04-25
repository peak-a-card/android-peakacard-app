package com.peakacard.core.extensions.statistics

/**
 * Credits to: https://github.com/thomasnield/kotlin-statistics
 */
fun <T> Sequence<T>.mode() = countBy()
  .entries
  .asSequence()
  .sortedByDescending { it.value }
  .toList().let { list ->
    list.asSequence()
      .takeWhile { list[0].value == it.value }
      .map { it.key }
  }

fun <T> Iterable<T>.mode() = asSequence().mode()

//AGGREGATION OPERATORS

/**
 * Groups each distinct value with the number counts it appeared
 */
fun <T> Sequence<T>.countBy() = groupApply({ it }, {it.count()})
