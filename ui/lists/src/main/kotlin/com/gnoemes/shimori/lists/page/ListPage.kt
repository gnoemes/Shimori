package com.gnoemes.shimori.lists.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.api.UiMessage
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.compose.theme.dimens
import com.gnoemes.shimori.common.compose.ui.*
import com.gnoemes.shimori.common.compose.ui.FabPosition
import com.gnoemes.shimori.common.extensions.rememberStateWithLifecycle
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.lists.sort.ListSort
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.user.UserShort
import kotlinx.coroutines.delay

@Composable
internal fun ListPage(
    type: ListType,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {

    val viewModel = when (type) {
        ListType.Anime -> animeViewModel()
        ListType.Manga -> mangaViewModel()
        ListType.Ranobe -> ranobeViewModel()
        ListType.Pinned -> pinViewModel()
        else -> throw IllegalStateException()
    }

    ListPage(
        viewModel = viewModel,
        onChangeList = onChangeList,
        openSearch = openSearch,
        openUser = openUser,
        openListsEdit = openListsEdit,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPage(
    viewModel: BasePageViewModel,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val snackbarHostState = rememberSnackbarHostState()

    val onEditClick =
        { entity: EntityWithRate<out ShimoriEntity> -> openListsEdit(entity.id, entity.type) }

    val onTogglePin = { entity: EntityWithRate<out ShimoriEntity> -> viewModel.togglePin(entity) }

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

    val itemCard = @Composable { entity: EntityWithRate<out ShimoriEntity> ->
        when (entity) {
            is AnimeWithRate -> {
                AnimeListCard(anime = entity,
                    onCoverLongClick = { onTogglePin(entity) },
                    onEditClick = { onEditClick(entity) },
                    onIncrementClick = { /*TODO*/ },
                    onIncrementHold = {})
            }
            is MangaWithRate -> {
                MangaListCard(manga = entity,
                    onCoverLongClick = { onTogglePin(entity) },
                    onEditClick = { onEditClick(entity) },
                    onIncrementClick = { /*TODO*/ },
                    onIncrementHold = {})
            }
            is RanobeWithRate -> {
                RanobeListCard(ranobe = entity,
                    onCoverLongClick = { onTogglePin(entity) },
                    onEditClick = { onEditClick(entity) },
                    onIncrementClick = { /*TODO*/ },
                    onIncrementHold = {})
            }
            else -> Unit
        }
    }

    if (viewModel is PinPageViewModel) {
        val listItems by rememberStateWithLifecycle(viewModel.list)

        ScreenLayout(
            type = state.type,
            status = state.status,
            user = state.user,
            snackbarHostState = snackbarHostState,
            scrollBehavior = scrollBehavior,
            message = state.message,
            onChangeList = onChangeList,
            openSearch = openSearch,
            openUser = openUser
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
                itemSpacer(paddingValues.calculateTopPadding())
                item("sort") { ListSort() }

                items(listItems) { entity -> itemCard(entity) }

                itemSpacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        //bottomBar + FAB
                        .height(bottomBarHeight + 52.dp)
                )
            }
        }
    } else {
        val pagingItems = when (viewModel) {
            is AnimePageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
            is MangaPageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
            is RanobePageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
            else -> throw IllegalStateException("$viewModel doesn't support pagination")
        }

        pagingItems.loadState.prependErrorOrNull()?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message.message)
            }
        }
        pagingItems.loadState.appendErrorOrNull()?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message.message)
            }
        }
        pagingItems.loadState.refreshErrorOrNull()?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message.message)
            }
        }

        ScreenLayout(
            type = state.type,
            status = state.status,
            user = state.user,
            snackbarHostState = snackbarHostState,
            scrollBehavior = scrollBehavior,
            message = state.message,
            onChangeList = onChangeList,
            openSearch = openSearch,
            openUser = openUser
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .fillMaxSize()
            ) {
//                scroll state can't be restored if we using header/footers with paging data
//                https://issuetracker.google.com/issues/177245496
                if (pagingItems.itemCount == 0 && pagingItems.loadState.refresh is LoadState.NotLoading) {
                    return@LazyColumn
                }

                itemSpacer(paddingValues.calculateTopPadding())
                item("sort") { ListSort() }

                items(pagingItems) { entity -> entity?.let { itemCard(it) } }

                itemSpacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        //bottomBar + FAB
                        .height(bottomBarHeight + 52.dp)
                )
            }
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
            ShimoriSnackbar(hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    val image = message?.shimoriImage
                    if (image != null) {
                        Image(
                            painter = rememberImagePainter(image),
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

@Composable
private fun animeViewModel(): AnimePageViewModel {
    return viewModel(
        modelClass = AnimePageViewModel::class.java, key = "anime-page", factory = createFactory()
    )
}

@Composable
private fun mangaViewModel(): MangaPageViewModel {
    return viewModel(
        modelClass = MangaPageViewModel::class.java, key = "manga-page", factory = createFactory()
    )
}

@Composable
private fun ranobeViewModel(): RanobePageViewModel {
    return viewModel(
        modelClass = RanobePageViewModel::class.java, key = "ranobe-page", factory = createFactory()
    )
}

@Composable
private fun pinViewModel(): PinPageViewModel {
    return viewModel(
        modelClass = PinPageViewModel::class.java, key = "pin-page", factory = createFactory()
    )
}

@Composable
private fun createFactory(): ViewModelProvider.Factory? {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    return if (viewModelStoreOwner is NavBackStackEntry) {
        HiltViewModelFactory(
            context = LocalContext.current, navBackStackEntry = viewModelStoreOwner
        )
    } else {
        // Use the default factory provided by the ViewModelStoreOwner
        // and assume it is an @AndroidEntryPoint annotated fragment or activity
        null
    }
}