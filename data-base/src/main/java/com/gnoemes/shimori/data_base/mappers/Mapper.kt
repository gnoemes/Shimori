package com.gnoemes.shimori.data_base.mappers

interface Mapper<F, T> {
    suspend fun map(from : F) : T
}