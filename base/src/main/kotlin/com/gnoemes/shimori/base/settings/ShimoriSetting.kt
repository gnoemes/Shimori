package com.gnoemes.shimori.base.settings

import kotlinx.coroutines.flow.Flow

interface Setting<T> {
    suspend fun update(newState: T)
    val observe: Flow<T>

    suspend operator fun invoke(newState: T) = update(newState = newState)
}