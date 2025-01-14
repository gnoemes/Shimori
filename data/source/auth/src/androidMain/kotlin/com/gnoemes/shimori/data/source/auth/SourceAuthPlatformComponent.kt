package com.gnoemes.shimori.data.source.auth

import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface SourceAuthPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideCodeFactory(): CodeFactoryHolder = AndroidCodeFactoryHolder.Instance

}

class AndroidCodeFactoryHolder private constructor() : CodeFactoryHolder {
    override val factory by lazy { AndroidCodeAuthFlowFactory() }

    companion object {
        val Instance = AndroidCodeFactoryHolder()
    }
}