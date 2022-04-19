package com.gnoemes.shimori.base.core.extensions

import io.ktor.http.*

public fun parametersOf(vararg pairs: Pair<String, String>): Parameters =
    ParametersImpl(pairs.map { it.second to listOf(it.second) }.toMap())