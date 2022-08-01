package com.gnoemes.shimori.lists

import androidx.compose.foundation.layout.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.user.UserShort
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

@OptIn(ExperimentalLifecycleComposeApi::class)
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

    when {
        state.isLoading -> ListsLoading()
        state.isEmpty -> ListsEmpty(
            type = state.type,
            hasRates = state.hasRates,
            user = state.user,
            openSearch = openSearch,
            openUser = openUser,
            onAnimeExplore = onAnimeExplore,
            onMangaExplore = onMangaExplore,
            onRanobeExplore = onRanobeExplore,
            onChangeList = onChangeList
        )
        else -> {
            ListPage(
                onChangeList = onChangeList,
                openListsEdit = openListsEdit,
                openSearch = openSearch,
                openUser = openUser
            )
        }
    }
}

//TODO add loading screen
@Composable
private fun ListsLoading() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ListsEmpty(
    type: ListType,
    hasRates: Boolean,
    user: UserShort?,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onChangeList: () -> Unit,
) {

    ScaffoldExtended(
        topBar = {
            ShimoriMainToolbar(
                modifier = Modifier,
                title = LocalShimoriRateUtil.current.listTypeName(type),
                user = user,
                onSearchClick = openSearch,
                onUserClick = openUser,
            )
        }
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

            if (type == ListType.Pinned && hasRates) {
                Spacer(modifier = Modifier.height(64.dp))

                EnlargedButton(
                    onClick = onChangeList,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    text = stringResource(id = R.string.lists_title),
                    leftIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = stringResource(id = R.string.lists_title)
                        )
                    }
                )
            }
        }
    }
}