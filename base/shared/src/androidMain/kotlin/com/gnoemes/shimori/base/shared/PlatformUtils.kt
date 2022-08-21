package com.gnoemes.shimori.base.shared

import io.github.aakira.napier.Antilog
import io.github.aakira.napier.DebugAntilog
import java.util.*


actual object PlatformUtils {
    actual fun generateUUID() : String = UUID.randomUUID().toString()
    actual fun generateId() : Long = UUID.randomUUID().mostSignificantBits
}