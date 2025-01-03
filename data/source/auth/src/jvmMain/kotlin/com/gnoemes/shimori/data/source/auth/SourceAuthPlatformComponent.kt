package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.JvmCodeAuthFlowFactory

actual interface SourceAuthPlatformComponent {

    @Provides
    @ApplicationScope
    fun provideCodeFactory(): CodeFactoryHolder = JvmCodeFactoryHolder.Instance

}

class JvmCodeFactoryHolder private constructor() : CodeFactoryHolder {
    override val factory by lazy { JvmCodeAuthFlowFactory() }

    companion object {
        val Instance = JvmCodeFactoryHolder()
    }
}