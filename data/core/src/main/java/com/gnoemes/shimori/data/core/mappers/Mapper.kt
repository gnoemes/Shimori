package com.gnoemes.shimori.data.core.mappers

fun interface Mapper<F, T> {
    suspend fun map(from : F) : T
}

interface TwoWayMapper<F, T> : Mapper<F, T> {
    suspend fun mapInverse(from: T): F
}