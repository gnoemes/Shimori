package com.gnoemes.shimori.lists.page

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.compose.AnimeListCard
import com.gnoemes.shimori.common.extensions.rememberFlowWithLifecycle
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dagger.hilt.android.EntryPointAccessors


@Composable
fun ListPage(
    status: RateStatus
) {
    ListStatusPage(viewModel = viewModel(factory = pageViewModel(status = status), key = "$status"))
}

@Composable
internal fun pageViewModel(status: RateStatus): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            ListPageViewModel.ViewModelFactoryProvider::class.java
    ).pageFactory()

    return ListPageViewModel.provideFactory(factory, status)
}

@Composable
internal fun ListStatusPage(
    viewModel: ListPageViewModel
) {
    val list = rememberFlowWithLifecycle(viewModel.list).collectAsLazyPagingItems()

    val actioner = { action: ListPageAction -> viewModel.submitAction(action) }

    PagingPage(list, actioner)
}

@Composable
internal fun PagingPage(
    list: LazyPagingItems<AnimeWithRate>,
    actioner: (ListPageAction) -> Unit
) {
    Page {
        items(list) { item ->
            if (item != null) {
                AnimeListCard(
                        anime = item,
                        onCoverLongClick = { item.entity.shikimoriId?.let { id -> actioner(ListPageAction.TogglePin(id, RateTargetType.ANIME)) } }
                )
            }
        }
    }
}

@Composable
internal fun Page(
    items: LazyListScope.() -> Unit
) {
    LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false,
                    additionalTop = 24.dp,
                    additionalBottom = 56.dp + 24.dp
            ),
            modifier = Modifier
                .fillMaxSize(),
            content = items
    )
}