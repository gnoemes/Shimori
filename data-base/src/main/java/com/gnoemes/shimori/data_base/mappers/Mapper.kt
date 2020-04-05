package com.gnoemes.shimori.data_base.mappers

interface Mapper<F, T> {
    suspend fun map(from : F) : T
}

interface TwoWayMapper<F, T> : Mapper<F, T> {
    suspend fun mapInverse(from: T): F
}