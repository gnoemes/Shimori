package com.gnoemes.shimori.tracks.list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.CollapsingAppBarNestedScrollConnection
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.LocalWindowSizeClass
import com.gnoemes.shimori.common.compose.isCompact
import com.gnoemes.shimori.common.compose.itemSpacer
import com.gnoemes.shimori.common.compose.ui.ShimoriSearchBar
import com.gnoemes.shimori.common.compose.ui.TrackItem
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_asc
import com.gnoemes.shimori.common.ui.resources.icons.ic_desc
import com.gnoemes.shimori.common.ui.resources.icons.ic_list
import com.gnoemes.shimori.common.ui.resources.strings.lists_title
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.data.TitleWithTrackEntity
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListSortOption
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.screens.TracksScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
@CircuitInject(screen = TracksScreen::class, scope = UiScope::class)
internal fun TracksUi(
    state: TracksUiState,
    modifier: Modifier = Modifier
) {
    val windowSizeClass = LocalWindowSizeClass.current
    val eventSink = state.eventSink


    TracksUi(
        widthSizeClass = windowSizeClass.widthSizeClass,
        state = state,
        addOneToProgress = { eventSink(TracksUiEvent.AddOneToProgress(it)) },
        changeSort = { eventSink(TracksUiEvent.ChangeSort(it)) },
        openEdit = { eventSink(TracksUiEvent.OpenEdit(it)) },
        openDetails = { eventSink(TracksUiEvent.OpenDetails(it)) },
        openMenu = { eventSink(TracksUiEvent.OpenMenu) },
        openSettings = { eventSink(TracksUiEvent.OpenSettings) },
    )
}

@Composable
private fun TracksUi(
    widthSizeClass: WindowWidthSizeClass,
    state: TracksUiState,
    addOneToProgress: (Track) -> Unit,
    changeSort: (ListSort) -> Unit,
    openEdit: (TitleWithTrackEntity) -> Unit,
    openDetails: (TitleWithTrackEntity) -> Unit,
    openMenu: () -> Unit,
    openSettings: () -> Unit,
) {
    val density = LocalDensity.current
    val isList = widthSizeClass.isCompact()
    val insets = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val appbarHeight = remember(isList) {
        if (isList) insets + 72.dp
        else 104.dp
    }
    val scrollConnection = remember(appbarHeight) {
        CollapsingAppBarNestedScrollConnection(with(density) { appbarHeight.roundToPx() })
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            AnimatedContent(widthSizeClass) { size ->
                if (size.isCompact()) {
                    ShimoriSearchBar(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset {
                                IntOffset(0, scrollConnection.appBarOffset)
                            },
                        openSettings = openSettings
                    )
                } else {
                    Row {
                        Spacer(Modifier.weight(1f))

                        ShimoriSearchBar(
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .widthIn(max = 328.dp)
                                .fillMaxWidth(),
                            openSettings = openSettings
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (state.isMenuVisible) {
                val expanded by remember {
                    derivedStateOf {
                        scrollConnection.appBarOffset != -scrollConnection.appBarMaxHeight
                    }
                }

                ExtendedFloatingActionButton(
                    text = {
                        Text(stringResource(Strings.lists_title))
                    },
                    icon = {
                        Icon(
                            painterResource(Icons.ic_list),
                            contentDescription = stringResource(Strings.lists_title)
                        )
                    },
                    expanded = expanded,
                    onClick = openMenu,
                )

            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            if (isList) {
                Box(modifier = Modifier.height(84.dp))
            }
        }
    ) {
        val items = state.items

//        if (items.itemCount == 0) {
//            CircuitContent(TracksEmptyScreen)
//        } else {
        TracksUiContent(
            scrollConnection = scrollConnection,
            paddingValues = it,
            widthSizeClass = widthSizeClass,
            type = state.type,
            status = state.status,
            sort = state.sort,
            items = items,
            sortOptions = state.sortOptions,
            firstSyncLoading = state.firstSyncLoading,
            addOneToProgress = addOneToProgress,
            changeSort = changeSort,
            openEdit = openEdit,
            openDetails = openDetails
        )
//        }
    }
}

@Composable
private fun TracksUiContent(
    scrollConnection: CollapsingAppBarNestedScrollConnection,
    paddingValues: PaddingValues,
    widthSizeClass: WindowWidthSizeClass,
    type: TrackTargetType,
    status: TrackStatus,
    sort: ListSort,
    items: LazyPagingItems<TitleWithTrackEntity>,
    sortOptions: List<ListSortOption>,
    firstSyncLoading: Boolean,
    addOneToProgress: (Track) -> Unit,
    changeSort: (ListSort) -> Unit,
    openEdit: (TitleWithTrackEntity) -> Unit,
    openDetails: (TitleWithTrackEntity) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val coroutineScope = rememberCoroutineScope()

    if (widthSizeClass.isCompact()) {
        val density = LocalDensity.current
        val spaceHeight by remember(density) {
            derivedStateOf {
                with(density) {
                    (scrollConnection.appBarMaxHeight + scrollConnection.appBarOffset).toDp()
                }
            }
        }


        Box(
            modifier = Modifier.nestedScroll(scrollConnection)
        ) {
            var parentWidth by remember { mutableStateOf(0.dp) }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .onGloballyPositioned { parentWidth = with(density) { it.size.width.toDp() } }
            ) {
                itemSpacer(spaceHeight + 8.dp, key = "scroll_spacer")

                item("header_list") {
                    Text(
                        textCreator {
                            "${status.name(type)} $divider ${type.name()}"
                        },
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }

                item("sort") {
                    val scrollState = rememberScrollState()
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .horizontalScroll(scrollState),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        SortOptions(
                            scrollState = scrollState,
                            sort = sort,
                            availableOptions = sortOptions,
                            onClick = {
                                changeSort(it)
                            }
                        )
                    }
                }

                items(
                    count = items.itemCount,
                    key = items.itemKey { "track_${it.track?.id}" },
                ) { index ->
                    val entity = items[index]
                    val track = entity?.track
                    if (entity != null && track != null) {
                        val openDetailsClick = remember(track.id) {
                            { openDetails(entity) }
                        }

                        val openEditClick = remember(track.id) {
                            { openEdit(entity) }
                        }

                        TrackItem(
                            parentWidth = parentWidth,
                            titleWithTrack = entity,
                            modifier = Modifier
                                .animateItem(placementSpec = null)
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            openDetails = openDetailsClick,
                            openEdit = openEditClick,
                            addOneToProgress = { addOneToProgress(track) }
                        )
                    }
                }

                itemSpacer(paddingValues.calculateBottomPadding() + 68.dp)
            }
        }


    } else {
        Column {

            LazyHorizontalGrid(
                rows = GridCells.FixedSize(158.dp)
            ) {

            }
        }
    }

}

@Composable
private fun RowScope.SortOptions(
    scrollState: ScrollState,
    sort: ListSort,
    availableOptions: List<ListSortOption>,
    onClick: (ListSort) -> Unit,
) {
    val textCreator = LocalShimoriTextCreator.current
    val scope = rememberCoroutineScope()
    var initialScrolled by remember(sort.sortOption) { mutableStateOf(false) }

    val offset = with(LocalDensity.current) {
        //offset + icon size
        (16 + 18).dp.roundToPx()
    }

    Spacer(Modifier.width(8.dp))
    availableOptions.forEach { option ->
        val selected = option == sort.sortOption
        val isDescending = sort.isDescending

        FilterChip(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                if (!initialScrolled && selected) {
                    scope.launch {
                        val position = coordinates.positionInParent().x.roundToInt()
                        scrollState.animateScrollTo(position - offset)
                    }

                    initialScrolled = true
                }
            },
            selected = selected,
            onClick = {
                if (selected) onClick(sort.copy(isDescending = !isDescending))
                else onClick(sort.copy(sortOption = option))
            },
            label = {
                Text(
                    textCreator {
                        option.name(sort.type)
                    }
                )
            },
            leadingIcon = {
                AnimatedVisibility(selected) {
                    Icon(
                        painterResource(if (isDescending) Icons.ic_asc else Icons.ic_desc),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                }
            }
        )
    }
    Spacer(Modifier.width(8.dp))
}


