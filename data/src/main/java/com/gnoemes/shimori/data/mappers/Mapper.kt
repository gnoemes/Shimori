package com.gnoemes.shimori.data.mappers

interface Mapper<F, T> {
    suspend fun map(from : F) : T
}