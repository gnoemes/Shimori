package com.gnoemes.shimori.common_ui_imageloading

import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.Size
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage


@ExperimentalCoilApi
class ShimoriImageInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = when (val data = chain.request.data) {
            is ShimoriImage -> {
                chain.request.newBuilder()
                    .data(map(data, chain.size))
                    .build()
            }
            else -> chain.request
        }
        return chain.proceed(request)
    }

    private fun map(data: ShimoriImage, size: Size): String? {
        //TODO condition for size change?
        return data.original
    }
}