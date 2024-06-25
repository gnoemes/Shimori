package com.gnoemes.shimori.home

import com.gnoemes.shimori.base.inject.ActivityScope
import me.tatarka.inject.annotations.Provides

interface RootUiComponent {
    @Provides
    @ActivityScope
    fun bindRootContent(impl : DefaultShimoriContent) : ShimoriContent = impl
}