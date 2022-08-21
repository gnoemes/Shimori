package com.gnoemes.shimori.data.paging

expect class PagingConfig(
    pageSize: Int,
    prefetchDistance: Int = pageSize,
    enablePlaceholders: Boolean = false,
    initialLoadSize: Int = pageSize * 3,
    maxSize: Int = Int.MAX_VALUE,
    jumpThreshold: Int = Int.MIN_VALUE
)