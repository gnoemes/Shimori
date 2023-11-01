package com.gnoemes.shimori.base.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch

inline fun <reified T1, reified T2, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    crossinline transform: suspend (T1?, T2?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(2) {
        false to null
    }

    arrayOf(
        flow, flow2
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    crossinline transform: suspend (T1?, T2?, T3?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(3) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3,
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(4) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(5) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    flow6: Flow<T6?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?, T6?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(6) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5, flow6
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                        array[5].let { if (it.first) it.second as T6 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    flow6: Flow<T6?>,
    flow7: Flow<T7?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?, T6?, T7?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(7) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5, flow6, flow7
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                        array[5].let { if (it.first) it.second as T6 else null },
                        array[6].let { if (it.first) it.second as T7 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    flow6: Flow<T6?>,
    flow7: Flow<T7?>,
    flow8: Flow<T8?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(8) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                        array[5].let { if (it.first) it.second as T6 else null },
                        array[6].let { if (it.first) it.second as T7 else null },
                        array[7].let { if (it.first) it.second as T8 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    flow6: Flow<T6?>,
    flow7: Flow<T7?>,
    flow8: Flow<T8?>,
    flow9: Flow<T9?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(9) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                        array[5].let { if (it.first) it.second as T6 else null },
                        array[6].let { if (it.first) it.second as T7 else null },
                        array[7].let { if (it.first) it.second as T8 else null },
                        array[8].let { if (it.first) it.second as T9 else null },
                    )
                )
            }
        }
    }
}

inline fun <reified T1, reified T2, reified T3, reified T4, reified T5, reified T6, reified T7, reified T8, reified T9, reified T10, reified R> instantCombine(
    flow: Flow<T1?>,
    flow2: Flow<T2?>,
    flow3: Flow<T3?>,
    flow4: Flow<T4?>,
    flow5: Flow<T5?>,
    flow6: Flow<T6?>,
    flow7: Flow<T7?>,
    flow8: Flow<T8?>,
    flow9: Flow<T9?>,
    flow10: Flow<T10?>,
    crossinline transform: suspend (T1?, T2?, T3?, T4?, T5?, T6?, T7?, T8?, T9?, T10?) -> R
): Flow<R> = channelFlow {
    val array = Array<Pair<Boolean, Any?>>(10) {
        false to null
    }

    arrayOf(
        flow, flow2, flow3, flow4, flow5, flow6, flow7, flow8, flow9, flow10
    ).forEachIndexed { index, flow ->
        launch {
            flow.collect { element ->
                array[index] = true to element
                send(
                    transform(
                        array[0].let { if (it.first) it.second as T1 else null },
                        array[1].let { if (it.first) it.second as T2 else null },
                        array[2].let { if (it.first) it.second as T3 else null },
                        array[3].let { if (it.first) it.second as T4 else null },
                        array[4].let { if (it.first) it.second as T5 else null },
                        array[5].let { if (it.first) it.second as T6 else null },
                        array[6].let { if (it.first) it.second as T7 else null },
                        array[7].let { if (it.first) it.second as T8 else null },
                        array[8].let { if (it.first) it.second as T9 else null },
                        array[9].let { if (it.first) it.second as T10 else null },
                    )
                )
            }
        }
    }
}