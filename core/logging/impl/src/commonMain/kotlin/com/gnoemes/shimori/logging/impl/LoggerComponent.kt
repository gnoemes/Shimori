package com.gnoemes.shimori.logging.impl

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

expect interface LoggerPlatformComponent

@ContributesTo(AppScope::class)
interface LoggerComponent : LoggerPlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun bindLogger(
        platform: ApplicationInfo
    ): Logger {
        return when {
            platform.debug -> createLogger()
            else -> MockLogger()
        }
    }

}