package com.gnoemes.shimori.base.shared

import com.gnoemes.shimori.base.core.utils.Logger
import io.github.aakira.napier.Antilog
import io.github.aakira.napier.Napier

internal expect object LoggerUtils {
    fun antilogFactory(): Antilog
}

fun createLogger(): Logger {
    Napier.base(antilog = LoggerUtils.antilogFactory())
    return LoggerImpl()
}

private class LoggerImpl : Logger {
    override fun v(message: String, tag: String?, t: Throwable?) {
        Napier.v(message, t, tag)
    }

    override fun v(t: Throwable) {
        Napier.v(throwable = t, message = "")
    }

    override fun i(message: String, tag: String?, t: Throwable?) {
        Napier.i(message, t, tag)
    }

    override fun i(t: Throwable) {
        Napier.i(throwable = t, message = "")
    }

    override fun e(message: String, tag: String?, t: Throwable?) {
        Napier.e(message, t, tag)
    }

    override fun e(t: Throwable) {
        Napier.e(throwable = t, message = "")
    }

    override fun d(message: String, tag: String?, t: Throwable?) {
        Napier.d(message, t, tag)
    }

    override fun d(t: Throwable) {
        Napier.d(throwable = t, message = "")
    }

    override fun w(message: String, tag: String?, t: Throwable?) {
        Napier.w(throwable = t, message = "")
    }

    override fun w(t: Throwable) {
        Napier.w(throwable = t, message = "")
    }
}