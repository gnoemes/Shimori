package com.gnoemes.shikimori

internal const val MAX_PAGE_SIZE = 5000
internal const val SHIKIMORI_BASE_URL = "https://shikimori.me"

internal fun String.appendHostIfNeed(host: String = SHIKIMORI_BASE_URL): String {
    return if (this.contains("http")) this else host + this
}