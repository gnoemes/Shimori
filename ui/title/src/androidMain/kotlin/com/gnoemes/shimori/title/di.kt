package com.gnoemes.shimori.title

import com.gnoemes.shimori.common.ui.utils.bindViewModel
import org.kodein.di.DI
import org.kodein.di.instance

actual val titleModule = DI.Module("title-module") {
    bindViewModel { savedStateHandle ->
        TitleDetailsViewModel(
            savedStateHandle,
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance()
        )
    }
}