package com.gnoemes.shimori.common.ui.api

data class OptionalContent<T>(
    val loaded: Boolean,
    val content: T
)