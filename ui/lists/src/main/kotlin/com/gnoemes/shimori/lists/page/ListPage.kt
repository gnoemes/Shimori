package com.gnoemes.shimori.lists.page

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.animeListCard
import com.gnoemes.shimori.common.compose.theme.alpha
import com.gnoemes.shimori.common.compose.theme.caption
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

    if (viewModel.page == ListsPage.PINNED && list.itemCount == 0) {
        EmptyPinListPage()
    } else {
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
}

@Composable
internal fun EmptyPinListPage() {
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
    ) {

        Spacer(modifier = Modifier.height(64.dp))

        Icon(
                painter = painterResource(id = R.drawable.ic_pin),
                contentDescription = stringResource(id = R.string.no_pinned_titles),
                modifier = Modifier
                    .size(96.dp)
                    .background(color = MaterialTheme.colors.alpha, shape = CircleShape)
                    .padding(24.dp)
                    .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = stringResource(id = R.string.no_pinned_titles),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = stringResource(id = R.string.no_pinned_titles_description),
                color = MaterialTheme.colors.caption,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
        )
    }
}