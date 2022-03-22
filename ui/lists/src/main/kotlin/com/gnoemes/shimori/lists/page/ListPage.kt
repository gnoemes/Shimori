package com.gnoemes.shimori.lists.page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.HiltViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.compose.ui.*
import com.gnoemes.shimori.common.extensions.rememberStateWithLifecycle
import com.gnoemes.shimori.lists.sort.ListSort
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateTargetType

@Composable
internal fun ListPage(
    type: ListType,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {

    val viewModel = when (type) {
        ListType.Anime -> animeViewModel()
        ListType.Manga -> mangaViewModel()
        ListType.Ranobe -> ranobeViewModel()
        else -> throw IllegalStateException()
    }

    ListPage(
        viewModel = viewModel,
        openSearch = openSearch,
        openUser = openUser,
        openListsEdit = openListsEdit,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListPage(
    viewModel: BasePageViewModel,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    val state by rememberStateWithLifecycle(viewModel.state)

    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val snackbarHostState = rememberSnackbarHostState()

    val pagingItems = (when (viewModel) {
        is AnimePageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
        is MangaPageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
        is RanobePageViewModel -> viewModel.pagedList.collectAsLazyPagingItems()
        else -> throw IllegalStateException("$viewModel doesn't support pagination")
    })

    val onEditClick =
        { entity: EntityWithRate<out ShimoriEntity> -> openListsEdit(entity.id, entity.type) }

    state.message?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)

            viewModel.onMessageShown(message.id)
        }
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

    ScaffoldExtended(
        topBar = {
            ShimoriMainToolbar(
                modifier = Modifier,
                title = LocalShimoriRateUtil.current.listTypeName(state.type),
                user = state.user,
                onSearchClick = openSearch,
                onUserClick = openUser,
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = {
            ShimoriSnackbar(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    ) { paddingValues ->
        val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight

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

            items(pagingItems) { entity ->
                if (entity == null) return@items

                when (entity) {
                    is AnimeWithRate -> {
                        AnimeListCard(
                            anime = entity,
                            onCoverLongClick = { /*TODO*/ },
                            onEditClick = { onEditClick(entity) },
                            onIncrementClick = { /*TODO*/ },
                            onIncrementHold = {}
                        )
                    }
                    is MangaWithRate -> {
                        MangaListCard(
                            manga = entity,
                            onCoverLongClick = { /*TODO*/ },
                            onEditClick = { onEditClick(entity) },
                            onIncrementClick = { /*TODO*/ },
                            onIncrementHold = {}
                        )
                    }
                    is RanobeWithRate -> {
                        RanobeListCard(
                            ranobe = entity,
                            onCoverLongClick = { /*TODO*/ },
                            onEditClick = { onEditClick(entity) },
                            onIncrementClick = { /*TODO*/ },
                            onIncrementHold = {}
                        )
                    }
                    else -> Unit
                }

            }

            itemSpacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(bottomBarHeight)
            )
        }
    }
}

@Composable
private fun animeViewModel(): AnimePageViewModel {
    return viewModel(
        modelClass = AnimePageViewModel::class.java,
        key = "anime-page",
        factory = createFactory()
    )
}

@Composable
private fun mangaViewModel(): MangaPageViewModel {
    return viewModel(
        modelClass = MangaPageViewModel::class.java,
        key = "manga-page",
        factory = createFactory()
    )
}

@Composable
private fun ranobeViewModel(): RanobePageViewModel {
    return viewModel(
        modelClass = RanobePageViewModel::class.java,
        key = "ranobe-page",
        factory = createFactory()
    )
}

@Composable
private fun createFactory(): ViewModelProvider.Factory? {
    val viewModelStoreOwner = LocalViewModelStoreOwner.current
    return if (viewModelStoreOwner is NavBackStackEntry) {
        HiltViewModelFactory(
            context = LocalContext.current,
            navBackStackEntry = viewModelStoreOwner
        )
    } else {
        // Use the default factory provided by the ViewModelStoreOwner
        // and assume it is an @AndroidEntryPoint annotated fragment or activity
        null
    }
}