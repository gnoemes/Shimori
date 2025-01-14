package com.gnoemes.shimori.data.source.auth

import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.JvmCodeAuthFlowFactory
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface SourceAuthPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideCodeFactory(): CodeFactoryHolder = JvmCodeFactoryHolder.Instance

}

class JvmCodeFactoryHolder private constructor() : CodeFactoryHolder {
    override val factory by lazy { JvmCodeAuthFlowFactory() }

    companion object {
        val Instance = JvmCodeFactoryHolder()
    }
}