package com.gnoemes.shimori.lists

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.lists.empty.ListsEmpty
import com.gnoemes.shimori.lists.page.ListPage
import kotlinx.coroutines.delay

@Composable
fun Lists(
    openUser: () -> Unit,
    openSearch: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType, markComplete: Boolean) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onChangeList: () -> Unit,
    openTitleDetails: (id: Long, type: RateTargetType) -> Unit,
) {
    Lists(
        viewModel = shimoriViewModel(),
        openUser = openUser,
        openSearch = openSearch,
        openListsEdit = openListsEdit,
        onAnimeExplore = onAnimeExplore,
        onMangaExplore = onMangaExplore,
        onRanobeExplore = onRanobeExplore,
        onChangeList = onChangeList,
        openTitleDetails = openTitleDetails,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Lists(
    viewModel: ListsViewModel,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType, markComplete: Boolean) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onChangeList: () -> Unit,
    openTitleDetails: (id: Long, type: RateTargetType) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    val appBarState = rememberTopAppBarState()
    val pinBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        state = appBarState,
    )
    val scrollBehavior = remember { pinBehavior }

    val snackbarHostState = rememberSnackbarHostState()

    val message = state.message
    message?.let {
        LaunchedEffect(message) {
            val result =
                snackbarHostState.showSnackbar(
                    message.message,
                    actionLabel = message.action,
                    duration = SnackbarDuration.Short
                )

            if (result == SnackbarResult.ActionPerformed) {
                viewModel.onMessageAction(message.id)
            }

            delay(100L)
            viewModel.onMessageShown(message.id)
        }
    }

    ScaffoldExtended(
        topBar = {
            val type = state.type
            val title = when {
                type == ListType.Pinned -> null
                state.isEmpty -> null
                else -> state.type.rateType
            }?.let {
                LocalShimoriTextCreator.current.rateStatusText(it, state.status)
            } ?: LocalShimoriRateUtil.current.listTypeName(type)

            ShimoriMainToolbar(
                modifier = Modifier,
                title = title,
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
                    .fillMaxWidth(),
                icon = {
                    val image = message?.image
                    if (image != null) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(image)
                                .build(),
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
                    }
                })
        },
        floatingActionButton = {
            val onChangeListState by rememberUpdatedState(newValue = onChangeList)
            if ((!state.isEmpty || state.type == ListType.Pinned) && state.hasRates) {
                ShimoriFAB(
                    onClick = onChangeListState,
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
            }
        },
        floatingActionButtonPosition = com.gnoemes.shimori.common.ui.components.FabPosition.Center,
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(MaterialTheme.dimens.bottomBarHeight)
            )
        },
    ) { paddingValues ->
        val paddingValuesState = remember { paddingValues }
        when {
            !state.isLoading && state.isEmpty -> ListsEmpty(
                type = state.type,
                onAnimeExplore = onAnimeExplore,
                onMangaExplore = onMangaExplore,
                onRanobeExplore = onRanobeExplore,
            )
            else -> {
                ListPage(
                    paddingValues = paddingValuesState,
                    scrollBehavior = scrollBehavior,
                    snackbarHostState = snackbarHostState,
                    openListsEdit = openListsEdit,
                    onAnimeExplore = onAnimeExplore,
                    onMangaExplore = onMangaExplore,
                    onRanobeExplore = onRanobeExplore,
                    openTitleDetails = openTitleDetails,
                )
            }
        }
    }
}
