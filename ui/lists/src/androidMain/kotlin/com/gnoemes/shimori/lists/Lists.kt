package com.gnoemes.shimori.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
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
                    onChangeList = onChangeList
                )
            }
        }
    }
}

@Composable
private fun ListsEmpty(
    type: ListType,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(
            modifier = Modifier
                .statusBarsPadding()
                .height(128.dp)
        )

        Icon(
            painter = painterResource(
                id = if (type == ListType.Pinned) R.drawable.ic_pin_big
                else R.drawable.ic_empty
            ),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                id = if (type == ListType.Pinned) R.string.no_pinned_titles
                else R.string.no_titles
            ),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(
                id = if (type == ListType.Pinned) R.string.no_pinned_titles_description
                else R.string.no_titles_description
            ),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            ShimoriChip(
                onClick = onAnimeExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.anime),
                icon = {
                    ListTypeIcon(ListType.Anime)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            ShimoriChip(
                onClick = onMangaExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.manga),
                icon = {
                    ListTypeIcon(ListType.Manga)
                }
            )

            Spacer(modifier = Modifier.width(8.dp))

            ShimoriChip(
                onClick = onRanobeExplore,
                modifier = Modifier.height(32.dp),
                text = stringResource(id = R.string.ranobe),
                icon = {
                    ListTypeIcon(ListType.Ranobe)
                }
            )
        }
    }
}