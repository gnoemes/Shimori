package com.gnoemes.shimori.lists.page

import AnimeListCard
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.compose.LocalShimoriDimensions
import com.gnoemes.shimori.common.compose.itemSpacer
import com.gnoemes.shimori.common.extensions.rememberFlowWithLifecycle
import com.gnoemes.shimori.lists.sort.ListSort
import com.gnoemes.shimori.model.rate.RateTargetType

@Composable
internal fun ListPage(
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior?,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    ListPage(
        viewModel = hiltViewModel(),
        paddingValues = paddingValues,
        scrollBehavior = scrollBehavior,
        openListsEdit = openListsEdit,
    )
}

@Composable
private fun ListPage(
    viewModel: ListPageViewModel,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior?,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {

    val animes = rememberFlowWithLifecycle(viewModel.pagedList).collectAsLazyPagingItems()

    val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .then(
                scrollBehavior?.let { Modifier.nestedScroll(it.nestedScrollConnection) } ?: Modifier
            ),
    ) {
        itemSpacer(paddingValues.calculateTopPadding())

        item("sort") { ListSort() }

        items(animes) { anime ->
            if (anime == null) return@items

            AnimeListCard(
                anime = anime,
                onCoverLongClick = { /*TODO*/ },
                onEditClick = { },
                onIncrementClick = { /*TODO*/ },
                onIncrementHold = {}
            )
        }

        itemSpacer(
            modifier = Modifier
                .navigationBarsPadding()
                .height(bottomBarHeight)
        )
    }
}