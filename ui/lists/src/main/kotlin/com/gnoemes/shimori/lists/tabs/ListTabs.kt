package com.gnoemes.shimori.lists.tabs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.subInfoStyle
import com.gnoemes.shimori.common.compose.theme.toolbar
import com.gnoemes.shimori.lists.tabs.page.AnimeListPage
import com.gnoemes.shimori.lists.tabs.page.MangaListPage
import com.gnoemes.shimori.lists.tabs.page.RanobeListPage
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.ui.Scaffold
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun ListTabs(
    toolbar: @Composable () -> Unit,
    sorts: @Composable () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    ListTabs(
            viewModel = hiltViewModel(),
            openListsEdit = openListsEdit,
            toolbar = toolbar,
            sorts = sorts
    )
}

@Composable
private fun ListTabs(
    viewModel: ListTabsViewModel,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
    toolbar: @Composable () -> Unit,
    sorts: @Composable () -> Unit,
) {

    val viewState by viewModel.state.collectAsState()

    val onPageChanged = { status: RateStatus -> viewModel.onPageChanged(status) }

    if (viewState.pages.isNotEmpty()) {
        ListTabs(
                openListsEdit,
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
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
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

    val toolbarHeight = 120.dp
    val toolbarHeightPx = with(LocalDensity.current) { toolbarHeight.roundToPx().toFloat() }
    val toolbarOffsetHeightPx = remember { mutableStateOf(0f) }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = toolbarOffsetHeightPx.value + delta
                toolbarOffsetHeightPx.value = newOffset.coerceIn(-toolbarHeightPx, 0f)

                return Offset.Zero
            }
        }
    }

    Scaffold(
            topBar = {
                Column(
                        modifier = Modifier.offset { IntOffset(x = 0, y = toolbarOffsetHeightPx.value.roundToInt()) }
                ) {
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
                            },
                    )
                }

            },
            backgroundColor = MaterialTheme.colors.primary,
    ) { paddingValues ->
        HorizontalPager(state = pagerState,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .nestedScroll(nestedScrollConnection)
        ) { page ->
            when (type) {
                RateTargetType.ANIME -> AnimeListPage(pages[page], openListsEdit)
                RateTargetType.MANGA -> MangaListPage(pages[page], openListsEdit)
                RateTargetType.RANOBE -> RanobeListPage(pages[page], openListsEdit)
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
            backgroundColor = MaterialTheme.colors.toolbar,
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

