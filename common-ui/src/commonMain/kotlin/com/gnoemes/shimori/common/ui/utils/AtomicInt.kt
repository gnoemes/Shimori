package com.gnoemes.shimori.common.ui.utils

expect class AtomicInt(initialValue: Int = 0) {
    fun get(): Int
    fun set(newValue: Int)
    fun incrementAndGet(): Int
    fun decrementAndGet(): Int

    fun addAndGet(delta: Int): Int
    fun compareAndSet(expected: Int, new: Int): Boolean
}