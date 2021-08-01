package com.gnoemes.shimori.lists.page

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.compose.animeListCard
import com.gnoemes.shimori.common.extensions.rememberFlowWithLifecycle
import com.gnoemes.shimori.model.rate.ListsPage
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dagger.hilt.android.EntryPointAccessors


@Composable
fun ListPage(
    page: ListsPage
) {
    ListPage(viewModel = viewModel(factory = pageViewModel(page = page), key = "$page"))
}

@Composable
internal fun pageViewModel(page: ListsPage): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            ListPageViewModel.ViewModelFactoryProvider::class.java
    ).pageFactory()

    return ListPageViewModel.provideFactory(factory, page)
}

@Composable
internal fun ListPage(
    viewModel: ListPageViewModel
) {
    val list = rememberFlowWithLifecycle(viewModel.list).collectAsLazyPagingItems()

    LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false,

                    additionalTop = 24.dp,
                    additionalBottom = 56.dp + 24.dp
            ),
            modifier = Modifier
                .fillMaxSize()
    ) {
        items(list) { item ->
            if (item != null) {
                animeListCard(anime = item)
            }
        }
    }
}