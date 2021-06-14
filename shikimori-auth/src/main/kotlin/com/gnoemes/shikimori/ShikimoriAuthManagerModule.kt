package com.gnoemes.shikimori

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class ShikimoriAuthManagerModule {
    @Binds
    abstract fun bindsShikimoriAuthManager(manager : ActivityShikimoriAuthManager) : ShikimoriAuthManager
}