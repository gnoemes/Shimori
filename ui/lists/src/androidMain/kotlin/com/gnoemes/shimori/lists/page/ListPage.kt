package com.gnoemes.shimori.lists.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.components.FabPosition
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.rememberStateWithLifecycle
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.lists.sort.ListSort
import kotlinx.coroutines.delay

@Composable
internal fun ListPage(
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    ListPage(
        viewModel = shimoriViewModel(),
        onChangeList = onChangeList,
        openSearch = openSearch,
        openUser = openUser,
        openListsEdit = openListsEdit,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPage(
    viewModel: ListPageViewModel,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(
            state = appBarState
        )
    }
    val snackbarHostState = rememberSnackbarHostState()

    val onEditClick = { entity: TitleWithRateEntity -> openListsEdit(entity.id, entity.type) }
    val onTogglePin = { entity: TitleWithRateEntity -> viewModel.togglePin(entity) }

    state.message?.let { message ->
        LaunchedEffect(message) {
            val result =
                snackbarHostState.showSnackbar(message.message, actionLabel = message.action)

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onMessageAction(message.id)
            }

            delay(100L)
            viewModel.onMessageShown(message.id)
        }
    }

    val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight

    val listItems = viewModel.items.collectAsLazyPagingItems()

    listItems.loadState.prependErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }
    listItems.loadState.appendErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }
    listItems.loadState.refreshErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }

    ScreenLayout(
        type = state.type,
        status = state.status,
        user = state.user,
        message = state.message,
        snackbarHostState = snackbarHostState,
        scrollBehavior = scrollBehavior,
        onChangeList = onChangeList,
        openSearch = openSearch,
        openUser = openUser
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
        ) {
//                scroll state can't be restored if we using header/footers with paging data
//                https://issuetracker.google.com/issues/177245496
            if (listItems.itemCount == 0 && listItems.loadState.refresh is LoadState.NotLoading) {
                return@LazyColumn
            }

            itemSpacer(paddingValues.calculateTopPadding())
            item("sort") { ListSort() }

            items(listItems) { entity ->
                if (entity != null) {
                    ListCard(
                        title = entity,
                        onCoverLongClick = { onTogglePin(entity) },
                        onEditClick = { onEditClick(entity) },
                        onIncrementClick = { /*TODO*/ },
                        onIncrementHold = { /*TODO*/ })
                }
            }

            itemSpacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    //bottomBar + FAB
                    .height(bottomBarHeight + 52.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenLayout(
    type: ListType,
    status: RateStatus,
    user: UserShort?,
    message: UiMessage?,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ScaffoldExtended(
        topBar = {
            val title = when (type) {
                ListType.Pinned -> null
                else -> type.rateType
            }?.let { type ->
                LocalShimoriTextCreator.current.rateStatusText(type, status)
            } ?: LocalShimoriRateUtil.current.listTypeName(type)

            ShimoriMainToolbar(
                modifier = Modifier,
                title = title,
                user = user,
                onSearchClick = openSearch,
                onUserClick = openUser,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            ShimoriSnackbar(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    val image = message?.image
                    if (image != null) {
                        Image(
                            painter = rememberAsyncImagePainter(image),
                            contentScale = ContentScale.Crop,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.12f),
                                    ShimoriSmallestRoundedCornerShape
                                )
                                .clip(ShimoriSmallestRoundedCornerShape)
                        )

                        Spacer(modifier = Modifier.width(12.dp))
                    }
                })
        },
        floatingActionButton = {
            ShimoriFAB(
                onClick = onChangeList,
                expanded = true,
                modifier = Modifier
                    .height(40.dp),
                text = stringResource(id = R.string.lists_title),
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_menu),
                        contentDescription = stringResource(id = R.string.lists_title)
                    )
                }
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(MaterialTheme.dimens.bottomBarHeight)
            )
        },
        content = content
    )
}