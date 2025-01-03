package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.source.auth.store.DefaultAuthStore
import com.gnoemes.shimori.data.source.auth.store.SourceAuthStore
import com.gnoemes.shimori.preferences.AppAuthObservablePreferences
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

expect interface SourceAuthPlatformComponent

interface SourceAuthComponent : SourceAuthPlatformComponent {
    val factory: CodeAuthFlowFactory

    @ApplicationScope
    @Provides
    fun provideAuthStore(bind: AppAuthObservablePreferences): SourceAuthStore =
        DefaultAuthStore(bind)

    @Provides
    //di is bugged without extra level of abstraction via CodeFactoryHolder
    fun provideCodeFactory(holder: CodeFactoryHolder): CodeAuthFlowFactory = holder.factory
}

interface CodeFactoryHolder {
    val factory: CodeAuthFlowFactory
}