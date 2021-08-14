package com.gnoemes.shimori.domain

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gnoemes.shimori.base.entities.InvokeError
import com.gnoemes.shimori.base.entities.InvokeStarted
import com.gnoemes.shimori.base.entities.InvokeStatus
import com.gnoemes.shimori.base.entities.InvokeSuccess
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withTimeout
import java.util.concurrent.TimeUnit

abstract class Interactor<in P> {

    operator fun invoke(params: P, timeoutMs: Long = defaultTimeoutMs): Flow<InvokeStatus> {
        return flow {
            withTimeout(timeoutMs) {
                emit(InvokeStarted)
                doWork(params)
                emit(InvokeSuccess)
            }
        }.catch { t ->
            emit(InvokeError(t))
        }
    }

    suspend fun executeSync(params: P) = doWork(params)

    protected abstract suspend fun doWork(params: P)

    companion object {
        private val defaultTimeoutMs = TimeUnit.MINUTES.toMillis(5)
    }
}

abstract class PagingInteractor<P : PagingInteractor.Parameters<T>, T : Any> : SubjectInteractor<P, PagingData<T>>() {
    interface Parameters<T : Any> {
        val pagingConfig: PagingConfig
    }
}

abstract class SuspendingWorkInteractor<P : Any, T : Any> : SubjectInteractor<P, T>() {
    override fun createObservable(params: P): Flow<T> = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): T
}

abstract class SubjectInteractor<P : Any, T> {
    private val paramState = MutableSharedFlow<P>(
            replay = 1,
            extraBufferCapacity = 1,
            onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow: Flow<T> = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<T>
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
