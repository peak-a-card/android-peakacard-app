package com.peakacard.core.extensions.statistics

/**
 * Credits to: https://github.com/thomasnield/kotlin-statistics
 */
inline fun <T,K,R> Sequence<T>.groupApply(crossinline keySelector: (T) -> K, crossinline aggregation: (Iterable<T>) -> R): Map<K, R> {
  val map = mutableMapOf<K,MutableList<T>>()

  for (item in this) {
    val key = keySelector(item)
    val list = map.computeIfAbsent(key) { mutableListOf() }
    list += item
  }
  val aggregatedMap = mutableMapOf<K,R>()

  for ((key, value) in map) {
    aggregatedMap.put(key, aggregation(value))
  }
  return aggregatedMap
}
