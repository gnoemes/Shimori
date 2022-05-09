package com.gnoemes.shimori.lists.edit

import com.gnoemes.shimori.common.ui.utils.bindViewModel
import org.kodein.di.DI
import org.kodein.di.instance

actual val listsEditModule = DI.Module("lists-edit-module") {
    bindViewModel { handle ->
        ListsEditViewModel(
            handle,
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }
}