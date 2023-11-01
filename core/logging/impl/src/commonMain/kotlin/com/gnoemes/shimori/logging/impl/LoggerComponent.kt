package com.gnoemes.shimori.logging.impl

import com.gnoemes.shimori.base.entities.PlatformInfo
import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Provides

expect interface LoggerPlatformComponent

interface LoggerComponent : LoggerPlatformComponent {

    @ApplicationScope
    @Provides
    fun bindLogger(
        platform: PlatformInfo
    ) : Logger {
        return when {
            platform.debug -> createLogger()
            else -> MockLogger()
        }
    }

}