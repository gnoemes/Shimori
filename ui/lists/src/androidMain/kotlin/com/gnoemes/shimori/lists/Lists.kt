package com.gnoemes.shimori.lists

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.ScaffoldExtended
import com.gnoemes.shimori.common.ui.components.ShimoriFAB
import com.gnoemes.shimori.common.ui.components.ShimoriMainToolbar
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.lists.empty.ListsEmpty
import com.gnoemes.shimori.lists.page.ListPage

@Composable
fun Lists(
    openUser: () -> Unit,
    openSearch: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onChangeList: () -> Unit,
) {
    Lists(
        viewModel = shimoriViewModel(),
        openUser = openUser,
        openSearch = openSearch,
        openListsEdit = openListsEdit,
        onAnimeExplore = onAnimeExplore,
        onMangaExplore = onMangaExplore,
        onRanobeExplore = onRanobeExplore,
        onChangeList = onChangeList
    )
}

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun Lists(
    viewModel: ListsViewModel,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onChangeList: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(
            state = appBarState
        )
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
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(MaterialTheme.dimens.bottomBarHeight)
            )
        },
        floatingActionButton = {
            if (!state.isLoading && state.isEmpty) {
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
            }
        },
        floatingActionButtonPosition = com.gnoemes.shimori.common.ui.components.FabPosition.Center,
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
                    openListsEdit = openListsEdit,
                    onChangeList = onChangeList,
                    onAnimeExplore = onAnimeExplore,
                    onMangaExplore = onMangaExplore,
                    onRanobeExplore = onRanobeExplore,
                )
            }
        }
    }
}
