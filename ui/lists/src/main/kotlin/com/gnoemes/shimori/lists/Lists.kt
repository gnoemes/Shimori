package com.gnoemes.shimori.lists

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shikimori.entities.user.ShikimoriAuthState
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.*
import com.gnoemes.shimori.common.extensions.collectAsStateWithLifecycle
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateSortOption
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.user.User

@Composable
fun Lists(
    openUser: () -> Unit,
    openSearch: () -> Unit
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

    val viewState by viewModel.state.collectAsStateWithLifecycle(ListsViewState.Empty)

    Lists(
            viewState,
            openUser,
            openSearch
    ) { action ->
        viewModel.submitAction(action)
    }
}

@Composable
internal fun Lists(
    viewState: ListsViewState,
    openUser: () -> Unit,
    openSearch: () -> Unit,
    actioner: (ListsAction) -> Unit
) {

    Scaffold(
            topBar = {
                ListsTopBar(
                        title = LocalShimoriRateUtil.current.rateTargetTypeName(viewState.type),
                        authorized = viewState.authStatus == ShikimoriAuthState.LOGGED_IN,
                        user = viewState.user,
                        listType = viewState.type,
                        activeSort = viewState.activeSort,
                        sortsOptions = viewState.sorts,
                        openUser = openUser,
                        searchClick = openSearch,
                        onSortClick = { option, isDescending ->
                            actioner(ListsAction.UpdateListSort(option, isDescending))
                        }
                )
            }

    ) {

    }

}

@Composable
private fun ListsTopBar(
    title: String,
    authorized: Boolean,
    user: User?,
    listType: RateTargetType,
    activeSort: RateSort,
    sortsOptions: List<RateSortOption>,
    openUser: () -> Unit,
    searchClick: () -> Unit,
    onSortClick: (RateSortOption, Boolean) -> Unit,
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
            modifier = Modifier.height(32.dp),
            text = text,
            painter = painter
    )
}