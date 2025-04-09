package com.gnoemes.shimori.base.utils

fun <F, T> Mapper<F, T>.forLists(): suspend (List<F>) -> List<T> {
    return { list -> list.map { item -> map(item) } }
}

operator fun <F, T> Mapper<F, T>.invoke(from: F): T = map(from)
operator fun <F, T> TwoWayMapper<F, T>.invoke(from: T): F = mapInverse(from)
