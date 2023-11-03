package com.gnoemes.shimori.data.util

fun interface Mapper<F, T> {
    fun map(from: F): T
}

interface TwoWayMapper<F, T> : Mapper<F, T> {
    fun mapInverse(from: T): F
}