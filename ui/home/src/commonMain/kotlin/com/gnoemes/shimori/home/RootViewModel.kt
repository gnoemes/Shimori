package com.gnoemes.shimori.home

import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
class RootViewModel(
    @Assisted private val coroutineScope: CoroutineScope,
) {

}