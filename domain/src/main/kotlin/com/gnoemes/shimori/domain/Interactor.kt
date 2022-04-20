package com.gnoemes.shimori.domain

import com.gnoemes.shimori.base.core.entities.InvokeError
import com.gnoemes.shimori.base.core.entities.InvokeStarted
import com.gnoemes.shimori.base.core.entities.InvokeStatus
import com.gnoemes.shimori.base.core.entities.InvokeSuccess
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout

abstract class Interactor<in Params> {
    suspend fun executeSync(params: Params) = doWork(params)

    protected abstract suspend fun doWork(params: Params)

    operator fun invoke(params: Params, timeout: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        return flow {
            withTimeout(timeout) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        }.catch { emit(InvokeError(it)) }
    }

    companion object {
        //5 min
        private const val defaultTimeoutMs = 5 * 60 * 1000L
    }
}

abstract class SubjectInteractor<P : Any, T> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest { create(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun create(params: P): Flow<T>
}

abstract class ResultInteractor<in P, R> {
    operator fun invoke(params: P): Flow<R> = flow {
        emit(doWork(params))
    }

    suspend fun executeSync(params: P): R = doWork(params)

    protected abstract suspend fun doWork(params: P): R
}

operator fun Interactor<Unit>.invoke() = invoke(Unit)
operator fun <T> SubjectInteractor<Unit, T>.invoke() = invoke(Unit)