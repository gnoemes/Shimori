package com.gnoemes.shimori.lists.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.items
import cafe.adriel.voyager.kodein.rememberScreenModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.navigation.Tab
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.TitleWithTrack
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.track.*
import com.gnoemes.shimori.lists.INCREMENTATOR_MAX_PROGRESS
import com.gnoemes.shimori.lists.page.ListPageScreenModel
import com.gnoemes.shimori.lists.page.UiEvents
import com.gnoemes.shimori.lists.sort.ListSortScreenModel
import com.smarttoolfactory.gesture.pointerMotionEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun Tab.ListPage(
    listItems: LazyPagingItems<TitleWithTrack<out ShimoriTitleEntity>>,
    listState: LazyListState,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    openTrackEdit: (id: Long, type: TrackTargetType, markComplete: Boolean) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    openTitleDetails: (id: Long, type: TrackTargetType) -> Unit,
) {
    val screenModel = rememberScreenModel<ListPageScreenModel>()
    val sortScreenModel = rememberScreenModel<ListSortScreenModel>()
    val state by screenModel.state.collectAsState()

    val onEditClick = { entity: TitleWithTrackEntity ->
        openTrackEdit(
            entity.id,
            entity.type,
            false
        )
    }

    val onCardClick = { entity: TitleWithTrackEntity ->
        openTitleDetails(entity.id, entity.type)
    }
    val onTogglePin = { entity: TitleWithTrackEntity -> screenModel.togglePin(entity) }
    val onIncrementClick = { entity: TitleWithTrackEntity -> screenModel.showIncrementer(entity) }
    val onIncrementerProgress =
        { progress: Int -> screenModel.updateProgressFromIncrementer(progress) }

    LaunchedEffect(screenModel) {
        screenModel.uiEvents.collect {
            when (it) {
                is UiEvents.EditTrack -> openTrackEdit(
                    it.entity.id,
                    it.entity.type,
                    it.markComplete
                )
            }
        }
    }

    ScreenLayout {
        PaginatedList(
            listItems,
            listState,
            scrollBehavior,
            paddingValues,
            state.type,
            state.status,
            state.incrementerTitle,
            sortScreenModel,
            onEditClick,
            onTogglePin,
            onIncrementClick,
            onIncrementerProgress,
            onAnimeExplore,
            onMangaExplore,
            onRanobeExplore,
            onCardClick
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
@Composable
private fun PaginatedList(
    listItems: LazyPagingItems<TitleWithTrack<out ShimoriTitleEntity>>,
    listState: LazyListState,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues,
    type: ListType,
    status: TrackStatus,
    incrementerTitle: TitleWithTrackEntity?,
    sortScreenModel: ListSortScreenModel,
    onEditClick: (TitleWithTrackEntity) -> Unit,
    onTogglePin: (TitleWithTrackEntity) -> Unit,
    onIncrementClick: (TitleWithTrackEntity) -> Unit,
    onIncrementerProgress: (Int) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
    onCardClick: (TitleWithTrackEntity) -> Unit,
) {
    val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight

    val incrementerTitleState by rememberUpdatedState(newValue = incrementerTitle)
    var incrementerProgress by remember { mutableStateOf(0) }

    val closeIncrementer = { onIncrementerProgress(incrementerProgress) }

    LazyColumn(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            //non consumable pointer events
            .pointerMotionEvents(
                //detect any touch on list to close incrementer
                onDown = {
                    if (incrementerTitleState != null) {
                        closeIncrementer()
                    }
                },
                requireUnconsumed = false
            ),
        state = listState,
    ) {
        if (listItems.loadState.source.append is LoadState.NotLoading
            && listItems.loadState.source.append.endOfPaginationReached
            && listItems.itemCount == 0
        ) {

            item {
                ListsEmpty(
                    type = type,
                    onAnimeExplore = onAnimeExplore,
                    onMangaExplore = onMangaExplore,
                    onRanobeExplore = onRanobeExplore
                )
            }
            return@LazyColumn
        }

        itemSpacer(paddingValues.calculateTopPadding() + 24.dp)

        item("sort") { ListSort(sortScreenModel) }

        item("status_item_${ListType.Anime}") {
            CurrentStatusItem(type, status)
        }

        items(
            items = listItems,
            key = { "${it.type}-${it.id}" }
        ) { entity ->
            if (entity != null) {
                ListCard(
                    modifier = Modifier.animateItemPlacement(),
                    title = entity,
                    onClick = { onCardClick(entity) },
                    onCoverLongClick = { onTogglePin(entity) },
                    onEditClick = { onEditClick(entity) },
                    onIncrementClick = { onIncrementClick(entity) },
                )
            } else {
                LoadingItem()
            }
        }


        itemSpacer(
            modifier = Modifier
                .navigationBarsPadding()
                //bottomBar + FAB
                .height(bottomBarHeight + 64.dp)
        )
    }

    val offset = with(LocalDensity.current) { 48.dp.roundToPx() }

    AnimatedVisibility(
        visible = incrementerTitleState != null,
        enter = slideInHorizontally(initialOffsetX = { offset }) + fadeIn(),
        exit = fadeOut() + slideOutHorizontally(targetOffsetX = { offset })
    ) {
        val localIncrementerChange = { value: Int -> incrementerProgress = value }
        val scope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {

            //close incrementer after 5 sec if progress not changed
            LaunchedEffect(incrementerProgress) {
                delay(5000)
                scope.launch { closeIncrementer() }
            }

            Incrementer(
                image = incrementerTitle?.entity?.image,
                titleSize = incrementerTitle?.entity?.size,
                initialProgress = incrementerTitle?.track?.progress ?: 0,
                onProgressUpdated = localIncrementerChange
            )
        }
    }
}

@Composable
private fun BoxScope.Incrementer(
    image: ShimoriImage?,
    titleSize: Int?,
    initialProgress: Int,
    onProgressUpdated: (Int) -> Unit,
) {
    val incrementerHeight = 240.dp
    val incrementerHeightPx: Float
    val colorChangeHeights: ClosedRange<Float>

    val savedImage = remember { image }

    val actualSize = remember { titleSize ?: INCREMENTATOR_MAX_PROGRESS }

    with(LocalDensity.current) {
        incrementerHeightPx = incrementerHeight.toPx()
        colorChangeHeights = incrementerHeightPx - 48.dp.roundToPx()..incrementerHeightPx
    }

    //up to 50
    val maxProgress = remember {
        titleSize.let {
            if (initialProgress + INCREMENTATOR_MAX_PROGRESS >= actualSize) actualSize - initialProgress
            else INCREMENTATOR_MAX_PROGRESS
        }
    }

    //calculate height of single section
    val sectionHeightPx = remember(maxProgress) { incrementerHeightPx / maxProgress }

    var delta by remember { mutableStateOf(sectionHeightPx) }

    val state = rememberDraggableState { delta += it }
    val deltaCoerced = delta.coerceIn(0f, incrementerHeightPx)

    var currentSection by remember(initialProgress) { mutableStateOf(-1) }

    //increment by 1 initially
    currentSection = if (currentSection == -1) 1
    else (deltaCoerced / sectionHeightPx).toInt()

    onProgressUpdated(initialProgress + currentSection)
    Box(
        modifier = Modifier
            .padding(end = 16.dp)
            .clip(ShimoriSmallRoundedCornerShape)
            .height(incrementerHeight)
            .width(48.dp)
            .align(Alignment.CenterEnd)
            .minimumTouchTargetSize()
            .draggable(
                state = state,
                orientation = Orientation.Vertical,
                reverseDirection = true
            )
    ) {

        val inactiveColor = MaterialTheme.colorScheme.surfaceVariant
        val activeColor = MaterialTheme.colorScheme.contentColorFor(inactiveColor)

        val textColor = if (currentSection * sectionHeightPx in colorChangeHeights) {
            MaterialTheme.colorScheme.inverseOnSurface
        } else activeColor

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val sliderTop = Offset(
                size.width / 2f,
                (maxProgress - currentSection) * sectionHeightPx
            )
            val sliderBottom = Offset(size.width / 2f, incrementerHeightPx)

            //Inactive
            drawLine(
                color = inactiveColor,
                strokeWidth = size.width,
                start = Offset(size.width / 2f, 0f),
                end = sliderTop
            )

            //active
            drawLine(
                color = activeColor,
                strokeWidth = size.width,
                start = sliderTop,
                end = sliderBottom,
            )
        }

        Text(
            text = "+${currentSection}",
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier
                .padding(
                    top = 40.dp,
                    start = 8.dp,
                    end = 8.dp
                )
                .align(Alignment.TopCenter),
        )

        AsyncImage(
            model = ImageRequest
                .Builder(LocalContext.current)
                .data(savedImage)
                .apply {
                    crossfade(true)
                }.build(),
            modifier = Modifier
                .padding(start = 12.dp, top = 12.dp, end = 12.dp)
                .size(24.dp)
                .clip(ShimoriSmallestRoundedCornerShape),
            contentScale = ContentScale.Crop,
            contentDescription = null,
        )
    }
}