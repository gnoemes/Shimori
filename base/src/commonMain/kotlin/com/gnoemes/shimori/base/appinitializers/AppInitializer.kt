package com.gnoemes.shimori.base.appinitializers

interface AppInitializer<T> {
    fun init(context: T)
}