package com.gnoemes.shimori.base.extensions

import io.ktor.http.Parameters
import io.ktor.http.ParametersImpl

public fun parametersOf(vararg pairs: Pair<String, String>): Parameters =
    ParametersImpl(pairs.map { it.second to listOf(it.second) }.toMap())