package com.gnoemes.shimori.common.imageloading

import coil3.intercept.Interceptor
import coil3.request.ImageResult
import com.gnoemes.shimori.data.common.ShimoriImage
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding

@Inject
@ContributesBinding(AppScope::class, multibinding = true)
class ShimoriImageInterceptor : Interceptor {
    override suspend fun intercept(
        chain: Interceptor.Chain
    ): ImageResult = when (val data = chain.request.data) {
        is ShimoriImage -> handle(chain, data).proceed()
        else -> chain.proceed()
    }

    //TODO conditions for size change
    private suspend fun handle(
        chain: Interceptor.Chain,
        data: ShimoriImage
    ): Interceptor.Chain {

        //Just pass original for now
        val request = chain.request.newBuilder()
            .data(data.original)
            .build()

        return chain.withRequest(request)
    }
}