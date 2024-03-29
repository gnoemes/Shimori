package com.gnoemes.shimori.base.core.extensions

fun CharSequence?.firstUpperCase(): CharSequence? {
    return if (this.isNullOrBlank()) null
    else substring(0, 1).lowercase() + substring(1)
}
