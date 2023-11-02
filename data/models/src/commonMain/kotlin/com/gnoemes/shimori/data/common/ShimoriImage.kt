package com.gnoemes.shimori.data.common

@kotlinx.serialization.Serializable
data class ShimoriImage(
    val original: String?,
    val preview: String?,
    val x96: String? = null,
    val x48: String? = null
)