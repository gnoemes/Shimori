package com.gnoemes.shimori.base.shared

import io.github.aakira.napier.Antilog


expect object PlatformUtils {
    fun generateUUID(): String
    fun generateId(): Long
}


