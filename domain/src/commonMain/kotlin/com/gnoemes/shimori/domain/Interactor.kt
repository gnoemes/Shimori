package com.gnoemes.shimori.domain

import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.AppUiEvents
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes


abstract class Interactor<in P, R> {
    private val count = atomic(0)
    private val loadingState = MutableStateFlow(count.value)

    val inProgress: Flow<Boolean> = loadingState
        .map { it > 0 }
        .distinctUntilChanged()

    private fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    private fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }

    protected abstract suspend fun doWork(params: P): R

    suspend operator fun invoke(
        params: P,
        timeout: Duration = DefaultTimeout
    ): Result<R> = try {
        addLoader()
        runCatching {
            withTimeout(timeout) {
                doWork(params)
            }
        }
    } finally {
        removeLoader()
    }

    companion object {
        internal val DefaultTimeout = 5.minutes
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
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


abstract class PagingInteractor<P : PagingInteractor.PagingParams<T>, T : Any> :
    SubjectInteractor<P, PagingData<T>>() {
    interface PagingParams<T : Any> {
        val pagingConfig: PagingConfig
    }
}

suspend operator fun <R> Interactor<Unit, R>.invoke(
    timeout: Duration = Interactor.DefaultTimeout,
) = invoke(Unit, timeout)

operator fun <T> SubjectInteractor<Unit, T>.invoke() = invoke(Unit)

suspend fun <T> Result<T>.onFailurePublishToBus(): Result<T> {
    exceptionOrNull()?.let {
        EventBus.publish(AppUiEvents.UiError(it))
    }

    return this
}