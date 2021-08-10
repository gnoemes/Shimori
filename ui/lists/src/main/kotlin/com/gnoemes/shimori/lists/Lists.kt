package com.gnoemes.shimori.lists

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.gnoemes.shimori.lists.page.ListPage
import com.gnoemes.shimori.model.rate.ListsPage
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.user.UserShort
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

    if (viewState.authStatus.isAuthorized) {
        Lists(
                viewState,
                openUser,
                openSearch
        ) { action ->
            viewModel.submitAction(action)
        }
    } else {
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


        NeedAuthLists(
                signIn = { signInLauncher.launch(Unit) },
                signUp = { signUpLauncher.launch(Unit) }
        )
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

@OptIn(ExperimentalPagerApi::class)
@Composable
internal fun Lists(
    viewState: ListsViewState,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    actioner: (ListsAction) -> Unit
) {

    when {
        viewState.loading -> {
            ListsLoading(
                    title = LocalShimoriRateUtil.current.rateTargetTypeName(viewState.type),
                    authorized = viewState.authStatus.isAuthorized,
                    user = viewState.user,
                    openUser = openUser,
                    openSearch = openSearch
            )
        }
        viewState.pages.isEmpty() -> {
            //TODO add empty state
        }
        else -> {
            ListsLoaded(
                    type = viewState.type,
                    authorized = viewState.authStatus.isAuthorized,
                    user = viewState.user,
                    activeSort = viewState.activeSort,
                    sorts = viewState.sorts,
                    pages = viewState.pages,
                    openUser = openUser,
                    openSearch = openSearch,
                    onPageChanged = { actioner(ListsAction.UpdateCurrentPage(it)) },
                    onSortClick = { option, isDescending ->
                        actioner(ListsAction.UpdateListSort(option, isDescending))
                    }
            )
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
    Column() {
        RootScreenToolbar(
                title = title,
                showSearchButton = true,
                user = user,
                authorized = authorized,
                searchButtonClick = openSearch,
                avatarClick = openUser
        )

        //TODO add loading
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListsLoaded(
    type: RateTargetType,
    authorized: Boolean,
    user: UserShort?,
    activeSort: RateSort,
    sorts: List<RateSortOption>,
    pages: List<ListsPage>,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    onPageChanged: (ListsPage) -> Unit,
    onSortClick: (RateSortOption, Boolean) -> Unit
) {
    val pagerState =
        rememberPagerState(pageCount = pages.size, initialOffscreenLimit = pages.size.coerceAtLeast(1))

    LaunchedEffect(pagerState) {
        pagerState.pageChanges.collect { page ->
            val newPage = pages[page]
            onPageChanged(newPage)
        }
    }

    val scope = rememberCoroutineScope()

    Scaffold(
            topBar = {
                ListsTopBar(
                        title = LocalShimoriRateUtil.current.rateTargetTypeName(type),
                        authorized = authorized,
                        user = user,
                        listType = type,
                        activeSort = activeSort,
                        sortsOptions = sorts,
                        openUser = openUser,
                        searchClick = openSearch,
                        onSortClick = onSortClick,
                ) {
                    ListsTabs(
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

        Surface(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
        ) {
            HorizontalPager(state = pagerState,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
            ) { page ->
                ListPage(pages[page])
            }
        }
    }
}


@Composable
private fun ListsTopBar(
    title: String,
    authorized: Boolean,
    user: UserShort?,
    listType: RateTargetType,
    activeSort: RateSort,
    sortsOptions: List<RateSortOption>,
    openUser: () -> Unit,
    searchClick: () -> Unit,
    onSortClick: (RateSortOption, Boolean) -> Unit,
    content: @Composable () -> Unit
) {

    Column {
        RootScreenToolbar(
                title = title,
                showSearchButton = true,
                searchButtonClick = searchClick,
                user = user,
                authorized = authorized,
                avatarClick = openUser
        )

        SortOptions(
                listType = listType,
                activeSort = activeSort,
                sortsOptions = sortsOptions,
                onSortClick = onSortClick
        )

        content()
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
private fun ListsTabs(
    pagerState: PagerState,
    listType: RateTargetType,
    pages: List<ListsPage>,
    onClick: (Int) -> Unit
) {
    ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            backgroundColor = MaterialTheme.colors.primary,
            edgePadding = 0.dp,
            indicator = { tabPositions ->
                Indicator(
                        Modifier.pagerTabIndicatorOffsetFixedSize(pagerState, tabPositions)
                )
            },
            divider = {}
    ) {
        pages.forEachIndexed { index, listPage ->
            Tab(
                    text = {
                        Text(
                                text = LocalShimoriTextCreator.current.listsPageText(listType, listPage),
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
    listType: RateTargetType,
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