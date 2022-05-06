package com.gnoemes.shimori.lists

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.utils.bindViewModel
import com.gnoemes.shimori.lists.page.ListPageViewModel
import com.gnoemes.shimori.lists.sort.ListSortViewModel
import org.kodein.di.DI

actual val listsModule = DI.Module("lists-module") {
    bindViewModel { new(::ListsViewModel) }
    bindViewModel { new(::ListPageViewModel) }
    bindViewModel { new(::ListSortViewModel) }
}