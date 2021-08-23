package com.gnoemes.shimori.lists.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.subInfoStyle
import com.gnoemes.shimori.lists.tabs.page.AnimeListPage
import com.gnoemes.shimori.lists.tabs.page.MangaListPage
import com.gnoemes.shimori.lists.tabs.page.RanobeListPage
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun ListTabs(
    toolbar: @Composable () -> Unit,
    sorts: @Composable () -> Unit,
) {
    ListTabs(
            viewModel = hiltViewModel(),
            toolbar = toolbar,
            sorts = sorts
    )
}

@Composable
private fun ListTabs(
    viewModel: ListTabsViewModel,
    toolbar: @Composable () -> Unit,
    sorts: @Composable () -> Unit,
) {

    val viewState by viewModel.state.collectAsState()

    val onPageChanged = { status: RateStatus -> viewModel.onPageChanged(status) }

    if (viewState.pages.isNotEmpty()) {
        ListTabs(
                type = viewState.type,
                pages = viewState.pages,
                onPageChanged = onPageChanged,
                toolbar = toolbar,
                sorts = sorts
        )
    } else {
        //TODO empty pages state
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListTabs(
    type: RateTargetType,
    pages: List<RateStatus>,
    onPageChanged: (RateStatus) -> Unit,
    toolbar: @Composable () -> Unit,
    sorts: @Composable () -> Unit,
) {
    val pagerState =
        rememberPagerState(pageCount = pages.size, initialOffscreenLimit = pages.size.coerceAtLeast(1))

    LaunchedEffect(pagerState) {
        pagerState.pageChanges.collect { page ->
            val newPage = pages.getOrNull(page)
            newPage?.let(onPageChanged)
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
            topBar = {
                Column {
                    toolbar()
                    sorts()

                    Tabs(
                            pagerState = pagerState,
                            listType = type,
                            pages = pages,
                            onClick = { page ->
                                scope.launch {
                                    pagerState.animateScrollToPage(page = page)
                                }
                            }
                    )
                }

            },
            backgroundColor = MaterialTheme.colors.primary,
    ) { paddingValues ->
        HorizontalPager(state = pagerState,
                modifier = Modifier
                    .padding(top = paddingValues.calculateTopPadding())
                    .padding(horizontal = 16.dp)
        ) { page ->
            when (type) {
                RateTargetType.ANIME -> AnimeListPage(pages[page])
                RateTargetType.MANGA -> MangaListPage(pages[page])
                RateTargetType.RANOBE -> RanobeListPage(pages[page])
            }
        }
    }

}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun Tabs(
    pagerState: PagerState,
    listType: RateTargetType,
    pages: List<RateStatus>,
    onClick: (Int) -> Unit
) {
    CustomTabRow(
            modifier = Modifier.fillMaxWidth(),
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                Indicator(
                        Modifier.pagerTabIndicatorOffsetFixedSize(pagerState, tabPositions)
                )
            },
    ) {
        pages.forEachIndexed { index, listPage ->
            Tab(
                    text = {
                        Text(
                                text = LocalShimoriTextCreator.current.rateStatusText(listType, listPage),
                                style = MaterialTheme.typography.subInfoStyle,
                                color = MaterialTheme.colors.caption)
                    },
                    selected = pagerState.currentPage == index,
                    onClick = { onClick(index) },
            )
        }
    }
}

