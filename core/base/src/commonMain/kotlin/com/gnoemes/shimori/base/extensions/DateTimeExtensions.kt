package com.gnoemes.shimori.base.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration


inline val Duration.inPast: Instant
    get() = Clock.System.now() - this