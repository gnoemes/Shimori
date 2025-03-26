package com.gnoemes.shimori.tracks.list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
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
import com.gnoemes.shimori.common.compose.rememberLazyListState
import com.gnoemes.shimori.common.compose.ui.ShimoriSearchBar
import com.gnoemes.shimori.common.compose.ui.SideSheetDefaults
import com.gnoemes.shimori.common.compose.ui.TrackItem
import com.gnoemes.shimori.common.compose.ui.UiMessage
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
import com.gnoemes.shimori.screens.TracksEmptyScreen
import com.gnoemes.shimori.screens.TracksMenuScreen
import com.gnoemes.shimori.screens.TracksScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.foundation.CircuitContent
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

    val message = state.uiMessage

    TracksUi(
        widthSizeClass = windowSizeClass.widthSizeClass,
        state = state,
        message = message,
        onMessageShown = { eventSink(TracksUiEvent.ClearMessage(message?.id ?: 0)) },
        onMessageAction = { eventSink(TracksUiEvent.ActionMessage(message?.id ?: 0)) },
        addOneToProgress = { eventSink(TracksUiEvent.AddOneToProgress(it)) },
        changeSort = { eventSink(TracksUiEvent.ChangeSort(it)) },
        openEdit = { eventSink(TracksUiEvent.OpenEdit(it)) },
        openDetails = { eventSink(TracksUiEvent.OpenDetails(it)) },
        openMenu = { eventSink(TracksUiEvent.OpenMenu) },
        openSettings = { eventSink(TracksUiEvent.OpenSettings) },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun TracksUi(
    widthSizeClass: WindowWidthSizeClass,
    state: TracksUiState,
    message: UiMessage?,
    onMessageShown: () -> Unit,
    onMessageAction: () -> Unit,
    addOneToProgress: (Track) -> Unit,
    changeSort: (ListSort) -> Unit,
    openEdit: (TitleWithTrackEntity) -> Unit,
    openDetails: (TitleWithTrackEntity) -> Unit,
    openMenu: () -> Unit,
    openSettings: () -> Unit,
) {
    val density = LocalDensity.current
    val isList by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }
    val insets = with(density) { WindowInsets.statusBars.getTop(density).toDp() }
    val appbarHeight by remember(isList) {
        derivedStateOf {
            insets + 72.dp
        }
    }
    val scrollConnection = remember(appbarHeight) {
        CollapsingAppBarNestedScrollConnection(with(density) { appbarHeight.roundToPx() })
    }

    Row(
        modifier = Modifier.fillMaxSize()
            .animateContentSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxHeight().composed {
                if (state.isMenuVisible) weight(.75f)
                else fillMaxWidth()
            },
            topBar = {
                AnimatedVisibility(
                    isList,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    ShimoriSearchBar(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .offset {
                                IntOffset(0, scrollConnection.appBarOffset)
                            },
                        openSettings = openSettings
                    )
                }
            },
            floatingActionButton = {
                if (state.isMenuButtonVisible) {
                    val expanded by remember {
                        derivedStateOf {
                            scrollConnection.appBarOffset != -scrollConnection.appBarMaxHeight
                        }
                    }

                    ExtendedFloatingActionButton(
                        modifier = Modifier.navigationBarsPadding(),
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
            if (!state.itemsExist) {
                CircuitContent(TracksEmptyScreen)
            } else {
                TracksUiContent(
                    scrollConnection = scrollConnection,
                    paddingValues = it,
                    widthSizeClass = widthSizeClass,
                    type = state.type,
                    status = state.status,
                    sort = state.sort,
                    items = state.items,
                    sortOptions = state.sortOptions,
                    firstSyncLoading = state.firstSyncLoading,
                    addOneToProgress = addOneToProgress,
                    changeSort = changeSort,
                    openEdit = openEdit,
                    openDetails = openDetails
                )
            }
        }


        AnimatedVisibility(
            visible = state.isMenuVisible,
            enter = slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth * 2 },
                animationSpec = tween(durationMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth * 2 },
                animationSpec = tween(durationMillis = 300)
            ),
            modifier = Modifier.fillMaxHeight()
                .widthIn(max = SideSheetDefaults.SheetMaxWidth)
                .fillMaxWidth()
        ) {
            Row {
                Divider(
                    modifier = Modifier.fillMaxHeight()
                        .width(1.dp),
                    color = MaterialTheme.colorScheme.outlineVariant,
                    thickness = 0.5.dp
                )
                CircuitContent(TracksMenuScreen)
            }
        }
    }


}

@OptIn(ExperimentalLayoutApi::class)
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
    val density = LocalDensity.current
    val spaceHeight by remember(density) {
        derivedStateOf {
            with(density) {
                (scrollConnection.appBarMaxHeight + scrollConnection.appBarOffset).toDp()
            }
        }
    }
    val isList by remember(widthSizeClass) { derivedStateOf { widthSizeClass.isCompact() } }

    if (isList) {
        Box(
            modifier = Modifier.nestedScroll(scrollConnection)
        ) {
            var parentWidth by remember { mutableStateOf(0.dp) }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .onSizeChanged { parentWidth = with(density) { it.width.toDp() } },
                state = items.rememberLazyListState()
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
                    key = items.itemKey { "track_${it.id}" },
                ) { index ->
                    val entity = items[index]
                    val track = entity?.track
                    if (entity != null && track != null) {
                        val openDetailsClick = remember(entity.id) {
                            { openDetails(entity) }
                        }

                        val openEditClick = remember(entity.id) {
                            { openEdit(entity) }
                        }

                        TrackItem(
                            parentWidth = parentWidth,
                            titleWithTrack = entity,
                            modifier = Modifier
                                .animateItem(placementSpec = null)
                                .fillMaxWidth()
                                .clickable(onClick = openDetailsClick)
                                .padding(vertical = 12.dp),
                            openDetails = openDetailsClick,
                            openEdit = openEditClick,
                            addOneToProgress = { addOneToProgress(track) }
                        )
                    }
                }

                itemSpacer(paddingValues.calculateBottomPadding() + 92.dp)
            }
        }


    } else {
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            columns = GridCells.Adaptive(158.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item(
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                Column {
                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        textCreator {
                            "${status.name(type)} $divider ${type.name()}"
                        },
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CompositionLocalProvider(
                            LocalMinimumInteractiveComponentSize provides 0.dp
                        ) {
                            SortOptions(
                                scrollState = rememberScrollState(),
                                sort = sort,
                                availableOptions = sortOptions,
                                onClick = {
                                    changeSort(it)
                                }
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
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
                        parentWidth = Dp.Unspecified,
                        titleWithTrack = entity,
                        modifier = Modifier
                            .animateItem(placementSpec = null)
                            .animateContentSize()
                            .fillMaxWidth(),
                        openDetails = openDetailsClick,
                        openEdit = openEditClick,
                        addOneToProgress = { addOneToProgress(track) }
                    )
                }
            }

            item(
                span = { GridItemSpan(this.maxLineSpan) }
            ) {
                Spacer(Modifier.height(96.dp))
            }

        }

    }

}

@OptIn(ExperimentalLayoutApi::class)
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

    if (this !is FlowRowScope) Spacer(Modifier.width(8.dp))
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
    if (this !is FlowRowScope) Spacer(Modifier.width(8.dp))
}


