package com.gnoemes.shimori.lists.change

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.utils.bindViewModel
import com.gnoemes.shimori.lists.change.section.ListChangeStatusSectionViewModel
import org.kodein.di.DI
import org.kodein.di.instance

actual val listsChangeModule = DI.Module("lists-change-module") {
    bindViewModel { new(::ListsChangeViewModel) }
    bindViewModel { handle -> ListChangeStatusSectionViewModel(handle, instance(), instance()) }
}