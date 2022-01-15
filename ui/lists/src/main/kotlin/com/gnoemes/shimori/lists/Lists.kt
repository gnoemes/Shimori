//package com.gnoemes.shimori.lists
//
//import androidx.compose.animation.animateContentSize
//import androidx.compose.foundation.background
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.*
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import com.gnoemes.shimori.auth.Auth
//import com.gnoemes.shimori.common.R
//import com.gnoemes.shimori.common.compose.LocalShimoriRateUtil
//import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
//import com.gnoemes.shimori.common.compose.RootScreenToolbar
//import com.gnoemes.shimori.common.compose.ShimoriButton
//import com.gnoemes.shimori.common.compose.theme.alpha
//import com.gnoemes.shimori.common.compose.theme.caption
//import com.gnoemes.shimori.common.compose.theme.toolbar
//import com.gnoemes.shimori.lists.tabs.ListTabs
//import com.gnoemes.shimori.lists.tabs.page.pinned.ListPinnedPage
//import com.gnoemes.shimori.model.rate.ListType
//import com.gnoemes.shimori.model.rate.RateSort
//import com.gnoemes.shimori.model.rate.RateSortOption
//import com.gnoemes.shimori.model.rate.RateTargetType
//import com.gnoemes.shimori.model.user.UserShort
//import com.google.accompanist.insets.navigationBarsPadding
//import com.google.accompanist.pager.ExperimentalPagerApi
//
//@Composable
//fun Lists(
//    openUser: () -> Unit,
//    openSearch: () -> Unit,
//    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
//) {
//    Lists(
//            viewModel = hiltViewModel(),
//            openUser = openUser,
//            openSearch = openSearch,
//            openListsEdit = openListsEdit
//    )
//}
//
//@Composable
//internal fun Lists(
//    viewModel: ListsViewModel,
//    openUser: () -> Unit,
//    openSearch: () -> Unit,
//    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
//) {
//    val viewState by viewModel.state.collectAsState()
//
//    if (!viewState.authStatus.isAuthorized) {
//        Auth()
//    } else {
//        if (viewState.loading) {
//            ListsLoading(
//                    title = LocalShimoriRateUtil.current.listTypeName(viewState.type),
//                    user = viewState.user,
//                    openUser = openUser,
//                    openSearch = openSearch
//            )
//        } else {
//            val submit = { action: ListsAction -> viewModel.submitAction(action) }
//
//            Lists(
//                    viewState,
//                    openUser,
//                    openSearch,
//                    openListsEdit,
//                    onSortClick = { option, isDescending -> submit(ListsAction.UpdateListSort(option, isDescending)) },
//                    onEmptyAnimeClick = { submit(ListsAction.ListTypeChanged(ListType.Anime)) },
//                    onEmptyMangaClick = { submit(ListsAction.ListTypeChanged(ListType.Manga)) },
//                    onEmptyRanobeClick = { submit(ListsAction.ListTypeChanged(ListType.Ranobe)) },
//            )
//        }
//    }
//}
//
//@OptIn(ExperimentalPagerApi::class)
//@Composable
//internal fun Lists(
//    viewState: ListsViewState,
//    openUser: () -> Unit,
//    openSearch: () -> Unit,
//    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
//    onSortClick: (RateSortOption, Boolean) -> Unit,
//    onEmptyAnimeClick: () -> Unit,
//    onEmptyMangaClick: () -> Unit,
//    onEmptyRanobeClick: () -> Unit
//) {
//
//    val sorts = @Composable {
//        val sortsOptions = viewState.sorts
//        val notPinOrExist = viewState.type != ListType.Pinned || !viewState.noPinnedTitles
//        if (sortsOptions.isNotEmpty() && notPinOrExist) {
//            SortOptions(
//                    listType = viewState.type,
//                    activeSort = viewState.activeSort,
//                    sortsOptions = sortsOptions,
//                    onSortClick = onSortClick
//            )
//        }
//    }
//
//    val toolbar = @Composable {
//        RootScreenToolbar(
//                title = LocalShimoriRateUtil.current.listTypeName(viewState.type),
//                showSearchButton = true,
//                user = viewState.user,
//                searchButtonClick = openSearch,
//                avatarClick = openUser
//        )
//    }
//
//
//    when {
//        viewState.type == ListType.Pinned -> {
//            ListsPinned(
//                    noPinnedTitles = viewState.noPinnedTitles,
//                    onEmptyAnimeClick = onEmptyAnimeClick,
//                    onEmptyMangaClick = onEmptyMangaClick,
//                    onEmptyRanobeClick = onEmptyRanobeClick,
//                    openListsEdit = openListsEdit,
//                    topBar = toolbar
//            )
//        }
//        else -> {
//            ListTabs(
//                    toolbar = toolbar,
//                    sorts = sorts,
//                    openListsEdit = openListsEdit,
//            )
//        }
//    }
//}
//
//@Composable
//private fun ListsLoading(
//    title: String,
//    user: UserShort?,
//    openSearch: () -> Unit,
//    openUser: () -> Unit
//) {
//    Column {
//        RootScreenToolbar(
//                title = title,
//                showSearchButton = true,
//                user = user,
//                searchButtonClick = openSearch,
//                avatarClick = openUser
//        )
//
//        Box(modifier = Modifier
//            .fillMaxSize()
//            .navigationBarsPadding()
//        ) {
//            CircularProgressIndicator(
//                    color = MaterialTheme.colors.secondary,
//                    modifier = Modifier.align(Alignment.Center)
//            )
//        }
//    }
//}
//
//@Composable
//private fun ListsPinned(
//    noPinnedTitles: Boolean,
//    onEmptyAnimeClick: () -> Unit,
//    onEmptyMangaClick: () -> Unit,
//    onEmptyRanobeClick: () -> Unit,
//    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
//    topBar: @Composable () -> Unit,
//) {
//    Scaffold(
//            topBar = {
//                topBar()
//            },
//            backgroundColor = MaterialTheme.colors.primary,
//    ) { paddingValues ->
//        if (noPinnedTitles) {
//            ListsPinnedEmpty(
//                    onAnimeClick = onEmptyAnimeClick,
//                    onMangaClick = onEmptyMangaClick,
//                    onRanobeClick = onEmptyRanobeClick
//            )
//        } else {
//            Box(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .padding(paddingValues)
//                        .padding(horizontal = 16.dp)
//            ) {
//                ListPinnedPage(openListsEdit)
//            }
//        }
//    }
//}
//
//@Composable
//private fun ListsPinnedEmpty(
//    onAnimeClick: () -> Unit,
//    onMangaClick: () -> Unit,
//    onRanobeClick: () -> Unit,
//) {
//    Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp)
//    ) {
//        Spacer(modifier = Modifier.height(64.dp))
//
//        Icon(
//                painter = painterResource(id = R.drawable.ic_pin_big),
//                contentDescription = stringResource(id = R.string.no_pinned_titles),
//                modifier = Modifier
//                    .size(96.dp)
//                    .background(color = MaterialTheme.colors.alpha, shape = CircleShape)
//                    .padding(24.dp)
//                    .align(Alignment.CenterHorizontally)
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//                text = stringResource(id = R.string.no_pinned_titles),
//                color = MaterialTheme.colors.onPrimary,
//                style = MaterialTheme.typography.h2,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Text(
//                text = stringResource(id = R.string.no_pinned_titles_description),
//                color = MaterialTheme.colors.caption,
//                style = MaterialTheme.typography.caption,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
//        ) {
//            ShimoriButton(
//                    modifier = Modifier
//                        .heightIn(min = 32.dp)
//                        .weight(1f),
//                    selected = false,
//                    onClick = onAnimeClick,
//                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Anime),
//                    painter = painterResource(id = R.drawable.ic_anime),
//                    iconSize = 16.dp
//            )
//
//            ShimoriButton(
//                    modifier = Modifier
//                        .heightIn(min = 32.dp)
//                        .weight(1f),
//                    selected = false,
//                    onClick = onMangaClick,
//                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Manga),
//                    painter = painterResource(id = R.drawable.ic_manga),
//                    iconSize = 16.dp
//            )
//
//            ShimoriButton(
//                    modifier = Modifier
//                        .heightIn(min = 32.dp)
//                        .weight(1f),
//                    selected = false,
//                    onClick = onRanobeClick,
//                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Ranobe),
//                    painter = painterResource(id = R.drawable.ic_ranobe),
//                    iconSize = 16.dp
//            )
//        }
//    }
//}
//
//@Composable
//private fun SortOptions(
//    listType: ListType,
//    activeSort: RateSort,
//    sortsOptions: List<RateSortOption>,
//    onSortClick: (RateSortOption, Boolean) -> Unit
//) {
//    Row(
//            modifier = Modifier
//                .background(MaterialTheme.colors.toolbar)
//                .fillMaxWidth()
//                .height(56.dp)
//                .horizontalScroll(rememberScrollState())
//                .animateContentSize(),
//            verticalAlignment = Alignment.CenterVertically
//    ) {
//        Spacer(modifier = Modifier.width(16.dp))
//        sortsOptions.forEach { option ->
//            val isActiveSort = activeSort.sortOption == option
//            SortChip(
//                    text = LocalShimoriTextCreator.current.listSortText(listType, option),
//                    selected = isActiveSort,
//                    isDescending = isActiveSort && activeSort.isDescending,
//                    onClick = { onSortClick(option, activeSort.isDescending) },
//                    onReselect = { onSortClick(option, !activeSort.isDescending) }
//            )
//            Spacer(modifier = Modifier.width(8.dp))
//        }
//        Spacer(modifier = Modifier.width(8.dp))
//    }
//}
//
//@Composable
//private fun SortChip(
//    text: String,
//    selected: Boolean,
//    isDescending: Boolean,
//    onClick: () -> Unit,
//    onReselect: () -> Unit
//) {
//    val painter = if (selected) {
//        painterResource(if (isDescending) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
//    } else null
//
//    ShimoriButton(
//            selected = selected,
//            onClick = if (selected) onReselect else onClick,
//            modifier = Modifier.height(IntrinsicSize.Min),
//            text = text,
//            painter = painter,
//            iconSize = 16.dp
//    )
//}