package com.gnoemes.shimori.common.compose.ui

data class OptionalContent<T>(
    val loaded: Boolean,
    val content: T
)