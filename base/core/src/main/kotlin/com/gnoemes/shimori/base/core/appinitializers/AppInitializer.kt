package com.gnoemes.shimori.base.core.appinitializers

interface AppInitializer<T> {
    fun init(context: T)
}