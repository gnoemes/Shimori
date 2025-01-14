package com.gnoemes.shimori.logging.impl

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog

internal actual object LoggerUtils {
    actual fun antilogFactory(): Antilog = DebugAntilog()
}
