package com.gnoemes.shimori.lists

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.alpha
import com.gnoemes.shimori.common.compose.theme.caption
import com.gnoemes.shimori.common.compose.theme.subInfoStyle
import com.gnoemes.shimori.lists.page.AnimeListPage
import com.gnoemes.shimori.lists.page.MangaListPage
import com.gnoemes.shimori.lists.page.pinned.ListPinnedPage
import com.gnoemes.shimori.model.rate.*
import com.gnoemes.shimori.model.user.UserShort
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@Composable
fun Lists(
    openUser: () -> Unit,
    openSearch: () -> Unit,
) {
    Lists(
            viewModel = hiltViewModel(),
            openUser = openUser,
            openSearch = openSearch
    )
}

@Composable
internal fun Lists(
    viewModel: ListsViewModel,
    openUser: () -> Unit,
    openSearch: () -> Unit,
) {

    val viewState by viewModel.state.collectAsState()
    val signInLauncher =
        rememberLauncherForActivityResult(viewModel.buildLoginActivityResult()) { result ->
            if (result != null) {
                viewModel.onLoginResult(result)
            }
        }

    val signUpLauncher =
        rememberLauncherForActivityResult(viewModel.buildRegisterActivityResult()) { result ->
            if (result != null) {
                viewModel.onLoginResult(result)
            }
        }

    if (viewState.loading) {
        ListsLoading(
                title = LocalShimoriRateUtil.current.listTypeName(viewState.type),
                authorized = viewState.authStatus.isAuthorized,
                user = viewState.user,
                openUser = openUser,
                openSearch = openSearch
        )
    } else {
        val submit = { action: ListsAction -> viewModel.submitAction(action) }

        Lists(
                viewState,
                openUser,
                openSearch,
                signIn = { signInLauncher.launch(Unit) },
                signUp = { signUpLauncher.launch(Unit) },
                onPageChanged = { submit(ListsAction.PageChanged(it)) },
                onSortClick = { option, isDescending -> submit(ListsAction.UpdateListSort(option, isDescending)) },
                onEmptyAnimeClick = { submit(ListsAction.ListTypeChanged(ListType.Anime)) },
                onEmptyMangaClick = { submit(ListsAction.ListTypeChanged(ListType.Manga)) },
                onEmptyRanobeClick = { submit(ListsAction.ListTypeChanged(ListType.Ranobe)) },
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun Lists(
    viewState: ListsViewState,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    signIn: () -> Unit,
    signUp: () -> Unit,
    onPageChanged: (RateStatus) -> Unit,
    onSortClick: (RateSortOption, Boolean) -> Unit,
    onEmptyAnimeClick: () -> Unit,
    onEmptyMangaClick: () -> Unit,
    onEmptyRanobeClick: () -> Unit
) {

    val sorts = @Composable {
        val sortsOptions = viewState.sorts
        val notPinOrExist = viewState.type != ListType.Pinned || !viewState.noPinnedTitles
        if (sortsOptions.isNotEmpty() && notPinOrExist) {
            SortOptions(
                    listType = viewState.type,
                    activeSort = viewState.activeSort,
                    sortsOptions = sortsOptions,
                    onSortClick = onSortClick
            )
        }
    }

    val toolbar = @Composable {
        RootScreenToolbar(
                title = LocalShimoriRateUtil.current.listTypeName(viewState.type),
                showSearchButton = true,
                user = viewState.user,
                authorized = viewState.authStatus.isAuthorized,
                searchButtonClick = openSearch,
                avatarClick = openUser
        )
    }


    when {
        !viewState.authStatus.isAuthorized -> NeedAuthLists(signIn, signUp)
        viewState.pages.isEmpty() -> {
            //TODO add empty state
        }
        viewState.type == ListType.Pinned -> {
            ListsPinned(
                    noPinnedTitles = viewState.noPinnedTitles,
                    onEmptyAnimeClick = onEmptyAnimeClick,
                    onEmptyMangaClick = onEmptyMangaClick,
                    onEmptyRanobeClick = onEmptyRanobeClick,
                    topBar = toolbar
            )
        }
        else -> {
            ListsLoaded(
                    type = viewState.type,
                    pages = viewState.pages,
                    onPageChanged = onPageChanged,
                    toolbar = toolbar,
                    sorts = sorts
            )
        }
    }
}

@Composable
internal fun NeedAuthLists(
    signIn: () -> Unit,
    signUp: () -> Unit
) {

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(152.dp))

        Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = stringResource(id = R.string.profile),
                modifier = Modifier
                    .size(96.dp)
                    .background(color = MaterialTheme.colors.alpha, shape = CircleShape)
                    .padding(24.dp)
                    .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
                text = stringResource(id = R.string.sign_in_title),
                color = MaterialTheme.colors.onPrimary,
                style = MaterialTheme.typography.h2,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
                text = stringResource(id = R.string.sign_in_message),
                color = MaterialTheme.colors.caption,
                style = MaterialTheme.typography.caption,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(64.dp))

        EnlargedButton(
                selected = false,
                onClick = signIn,
                painter = painterResource(id = R.drawable.ic_sign_in),
                text = stringResource(id = R.string.sign_in),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
        ) {
            ChevronIcon()
        }

        Spacer(modifier = Modifier.height(8.dp))

        EnlargedButton(
                selected = false,
                onClick = signUp,
                painter = painterResource(id = R.drawable.ic_create_account),
                text = stringResource(id = R.string.sign_up),
                modifier = Modifier
                    .height(48.dp)
                    .fillMaxWidth()
        ) {
            ChevronIcon()
        }


    }
}

@Composable
private fun ListsLoading(
    title: String,
    user: UserShort?,
    authorized: Boolean,
    openSearch: () -> Unit,
    openUser: () -> Unit
) {
    Column {
        RootScreenToolbar(
                title = title,
                showSearchButton = true,
                user = user,
                authorized = authorized,
                searchButtonClick = openSearch,
                avatarClick = openUser
        )

        Box(modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
        ) {
            CircularProgressIndicator(
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
private fun ListsPinned(
    noPinnedTitles: Boolean,
    onEmptyAnimeClick: () -> Unit,
    onEmptyMangaClick: () -> Unit,
    onEmptyRanobeClick: () -> Unit,
    topBar: @Composable () -> Unit,
) {
    Scaffold(
            topBar = {
                topBar()
            },
            backgroundColor = MaterialTheme.colors.primary,
    ) { paddingValues ->
        if (noPinnedTitles) {
            ListsPinnedEmpty(
                    onAnimeClick = onEmptyAnimeClick,
                    onMangaClick = onEmptyMangaClick,
                    onRanobeClick = onEmptyRanobeClick
            )
        } else {
            Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
            ) {
                ListPinnedPage()
            }
        }
    }
}

@Composable
private fun ListsPinnedEmpty(
    onAnimeClick: () -> Unit,
    onMangaClick: () -> Unit,
    onRanobeClick: () -> Unit,
) {
    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(64.dp))

        Icon(
                painter = painterResource(id = R.drawable.ic_pin_big),
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

        Spacer(modifier = Modifier.height(32.dp))

        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            ShimoriButton(
                    modifier = Modifier
                        .heightIn(min = 32.dp)
                        .weight(1f),
                    selected = false,
                    onClick = onAnimeClick,
                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Anime),
                    painter = painterResource(id = R.drawable.ic_anime),
                    iconSize = 16.dp
            )

            ShimoriButton(
                    modifier = Modifier
                        .heightIn(min = 32.dp)
                        .weight(1f),
                    selected = false,
                    onClick = onMangaClick,
                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Manga),
                    painter = painterResource(id = R.drawable.ic_manga),
                    iconSize = 16.dp
            )

            ShimoriButton(
                    modifier = Modifier
                        .heightIn(min = 32.dp)
                        .weight(1f),
                    selected = false,
                    onClick = onRanobeClick,
                    text = LocalShimoriRateUtil.current.listTypeName(ListType.Ranobe),
                    painter = painterResource(id = R.drawable.ic_ranobe),
                    iconSize = 16.dp
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListsLoaded(
    type: ListType,
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

                    ListsTabs(
                            pagerState = pagerState,
                            listType = type.rateType!!,
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
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
        ) { page ->
            when (type) {
                ListType.Anime -> AnimeListPage(pages[page])
                ListType.Manga -> MangaListPage(pages[page])
            }
        }
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListsTabs(
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


@Composable
private fun SortOptions(
    listType: ListType,
    activeSort: RateSort,
    sortsOptions: List<RateSortOption>,
    onSortClick: (RateSortOption, Boolean) -> Unit
) {
    Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 12.dp)
                .horizontalScroll(rememberScrollState())
                .animateContentSize(),
    ) {
        Spacer(modifier = Modifier.width(16.dp))
        sortsOptions.forEach { option ->
            val isActiveSort = activeSort.sortOption == option
            SortChip(
                    text = LocalShimoriTextCreator.current.listSortText(listType, option),
                    selected = isActiveSort,
                    isDescending = isActiveSort && activeSort.isDescending,
                    onClick = { onSortClick(option, activeSort.isDescending) },
                    onReselect = { onSortClick(option, !activeSort.isDescending) }
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun SortChip(
    text: String,
    selected: Boolean,
    isDescending: Boolean,
    onClick: () -> Unit,
    onReselect: () -> Unit
) {
    val painter = if (selected) {
        painterResource(if (isDescending) R.drawable.ic_arrow_down else R.drawable.ic_arrow_up)
    } else null

    ShimoriButton(
            selected = selected,
            onClick = if (selected) onReselect else onClick,
            modifier = Modifier.heightIn(32.dp, 56.dp),
            text = text,
            painter = painter
    )
}