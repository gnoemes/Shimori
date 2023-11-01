package com.gnoemes.shimori.logging.impl

import com.gnoemes.shimori.logging.api.Logger
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier

internal expect object LoggerUtils {
    fun antilogFactory(): Antilog
}

internal fun createLogger(): Logger {
    Napier.base(antilog = LoggerUtils.antilogFactory())
    return LoggerImpl()
}

private class LoggerImpl : Logger {

    override fun v(throwable: Throwable?, tag: String?, message: () -> String) {
        Napier.v(throwable, tag, message)
    }

    override fun d(throwable: Throwable?, tag: String?, message: () -> String) {
        Napier.d(throwable, tag, message)
    }

    override fun i(throwable: Throwable?, tag: String?, message: () -> String) {
        Napier.i(throwable, tag, message)
    }

    override fun e(throwable: Throwable?, tag: String?, message: () -> String) {
        Napier.e(throwable, tag, message)
    }

    override fun w(throwable: Throwable?, tag: String?, message: () -> String) {
        Napier.w(throwable, tag, message)
    }
}

internal class MockLogger : Logger