package com.gnoemes.shimori.common.ui.utils

//TODO create multiplatform solution

@JvmInline
value class ImageID private constructor(val key: String) {
    companion object {
        val Tip = ImageID("tip")
    }
}