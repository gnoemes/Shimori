package com.gnoemes.shimori.base.entities

sealed class InvokeStatus {
    val isSuccess: Boolean get() = this is InvokeSuccess
    val isError: Boolean get() = this is InvokeError
}

object InvokeIdle : InvokeStatus()
object InvokeStarted : InvokeStatus()

object InvokeSuccess : InvokeStatus()

data class InvokeError(val throwable: Throwable) : InvokeStatus()
object InvokeTimeout : InvokeStatus()