package com.gnoemes.shimori.common.imageloading

import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.Size
import com.gnoemes.shimori.model.common.ShimoriImage
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject


@ExperimentalCoilApi
class ShimoriImageInterceptor @Inject constructor() : Interceptor {

    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        val request = when(val data = chain.request.data) {
            is ShimoriImage -> {
                chain.request.newBuilder()
                    .data(map(data, chain.size))
                    .build()
            }
            else -> chain.request
        }
        return chain.proceed(request)
    }

    private fun map(data : ShimoriImage, size : Size) : HttpUrl? {
        //TODO condition for size change?
        return data.original?.toHttpUrl()
    }
}