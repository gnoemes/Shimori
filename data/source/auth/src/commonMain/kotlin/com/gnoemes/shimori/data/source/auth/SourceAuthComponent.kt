package com.gnoemes.shimori.data.source.auth

import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.CodeAuthFlowFactory

expect interface SourceAuthPlatformComponent

interface SourceAuthComponent : SourceAuthPlatformComponent {
    val factory: CodeAuthFlowFactory

    @Provides
    //di is bugged without extra level of abstraction via CodeFactoryHolder
    fun provideCodeFactory(holder: CodeFactoryHolder): CodeAuthFlowFactory = holder.factory
}

interface CodeFactoryHolder {
    val factory: CodeAuthFlowFactory
}