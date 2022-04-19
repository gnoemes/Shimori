package com.gnoemes.shimori.common_ui_imageloading

import coil.annotation.ExperimentalCoilApi
import coil.intercept.Interceptor
import coil.request.ImageResult
import coil.size.Size


@ExperimentalCoilApi
class ShimoriImageInterceptor : Interceptor {
    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
        TODO("Restore me")
    }
    //
//    override suspend fun intercept(chain: Interceptor.Chain): ImageResult {
//        val request = when(val data = chain.request.data) {
//            is ShimoriImage -> {
//                chain.request.newBuilder()
//                    .data(map(data, chain.size))
//                    .build()
//            }
//            else -> chain.request
//        }
//        return chain.proceed(request)
//    }
//
//    private fun map(data : ShimoriImage, size : Size) : HttpUrl? {
//        //TODO condition for size change?
//        return data.original?.toHttpUrl()
//    }
}