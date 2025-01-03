package com.gnoemes.shimori.data.source.auth

import com.gnoemes.shimori.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory

actual interface SourceAuthPlatformComponent {

    @Provides
    @ApplicationScope
    fun provideCodeFactory(): CodeFactoryHolder = AndroidCodeFactoryHolder.Instance

}

class AndroidCodeFactoryHolder private constructor() : CodeFactoryHolder {
    override val factory by lazy { AndroidCodeAuthFlowFactory() }

    companion object {
        val Instance = AndroidCodeFactoryHolder()
    }
}