package com.gnoemes.shimori.source

import com.gnoemes.shimori.base.core.entities.SourcePlatformValues

interface Source {
    /**
     * Must be unique
     */
    val id: Long

    val name: String

    val lang: String get() = ""

    val values : SourcePlatformValues
}