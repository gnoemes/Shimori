package com.gnoemes.shimori.base.extensions

fun CharSequence?.firstUpperCase(): CharSequence? {
    return if (this.isNullOrBlank()) null
    else substring(0, 1).toUpperCase() + substring(1)
}
