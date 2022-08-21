package com.gnoemes.shimori.lists.page

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.theme.ShimoriBiggestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.ImageID
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.ShimoriTitleEntity
import com.gnoemes.shimori.data.core.entities.TitleWithRate
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.rate.*
import com.gnoemes.shimori.lists.INCREMENTATOR_MAX_PROGRESS
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.lists.empty.ListsEmpty
import com.gnoemes.shimori.lists.sort.ListSort
import com.smarttoolfactory.gesture.pointerMotionEvents
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ListPage(
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
    onChangeList: () -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
) {
    ListPage(
        viewModel = shimoriViewModel(),
        paddingValues = paddingValues,
        scrollBehavior = scrollBehavior,
        openListsEdit = openListsEdit,
        onChangeList = onChangeList,
        onAnimeExplore = onAnimeExplore,
        onMangaExplore = onMangaExplore,
        onRanobeExplore = onRanobeExplore,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
private fun ListPage(
    viewModel: ListPageViewModel,
    paddingValues: PaddingValues,
    scrollBehavior: TopAppBarScrollBehavior,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
    onChangeList: () -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = rememberSnackbarHostState()

    val onEditClick = { entity: TitleWithRateEntity -> openListsEdit(entity.id, entity.type) }
    val onTogglePin = { entity: TitleWithRateEntity -> viewModel.togglePin(entity) }
    val onIncrementClick = { viewModel.showIncrementerHint() }
    val onIncrementHold = { entity: TitleWithRateEntity -> viewModel.showIncrementer(entity) }
    val onIncrementerProgress =
        { progress: Int -> viewModel.updateProgressFromIncrementer(progress) }

    state.message?.let { message ->
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

    LaunchedEffect(viewModel) {
        viewModel.uiEvents.collect {
            when (it) {
                is UiEvents.EditRate -> onEditClick(it.entity)
            }
        }
    }


    val listItems = viewModel.items.collectAsLazyPagingItems()

    listItems.loadState.prependErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }
    listItems.loadState.appendErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }
    listItems.loadState.refreshErrorOrNull()?.let { message ->
        LaunchedEffect(message) {
            snackbarHostState.showSnackbar(message.message)
        }
    }

    ScreenLayout(
        snackbarHostState,
        state.message,
        onChangeList,
    ) {
        PaginatedList(
            listItems,
            scrollBehavior,
            paddingValues,
            state.type,
            state.incrementerTitle,
            state.isLoading,
            onEditClick,
            onTogglePin,
            onIncrementClick,
            onIncrementHold,
            onIncrementerProgress,
            onAnimeExplore,
            onMangaExplore,
            onRanobeExplore,
        )
    }
}

@ExperimentalMaterial3Api
@Composable
private fun PaginatedList(
    listItems: LazyPagingItems<TitleWithRate<out ShimoriTitleEntity>>,
    scrollBehavior: TopAppBarScrollBehavior,
    paddingValues: PaddingValues,
    type: ListType,
    incrementerTitle: TitleWithRateEntity?,
    isLoading: Boolean,
    onEditClick: (TitleWithRateEntity) -> Unit,
    onTogglePin: (TitleWithRateEntity) -> Unit,
    onIncrementClick: () -> Unit,
    onIncrementHold: (TitleWithRateEntity) -> Unit,
    onIncrementerProgress: (Int) -> Unit,
    onAnimeExplore: () -> Unit,
    onMangaExplore: () -> Unit,
    onRanobeExplore: () -> Unit,
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
        state = listItems.rememberLazyListState()
    ) {
        if (
            listItems.loadState.source.append is LoadState.NotLoading
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

        itemSpacer(paddingValues.calculateTopPadding())

        if (isLoading) {
            item { LoadingSort() }
            items(3) { LoadingItem() }
        } else {
            item("sort") { ListSort() }

            items(
                items = listItems,
                key = { "${it.type}-${it.id}" }
            ) { entity ->
                if (entity != null) {
                    ListCard(
                        title = entity,
                        onCoverLongClick = { onTogglePin(entity) },
                        onEditClick = { onEditClick(entity) },
                        onIncrementClick = { onIncrementClick() },
                        onIncrementHold = { onIncrementHold(entity) })
                } else {
                    LoadingItem()
                }
            }
        }

        itemSpacer(
            modifier = Modifier
                .navigationBarsPadding()
                //bottomBar + FAB
                .height(bottomBarHeight + 52.dp)
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
                initialProgress = incrementerTitle?.rate?.progress ?: 0,
                onProgressUpdated = localIncrementerChange
            )
        }
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
private fun LoadingSort() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .horizontalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.width(8.dp))

        val typeInt = LocalShimoriSettings.current.preferredListType.observe
            .collectAsStateWithLifecycle(initialValue = ListType.Anime.type)

        val type = ListType.findOrDefault(typeInt.value)
        val defaultSort = RateSort.defaultForType(type)

        RateSortOption.priorityForType(type).fastForEach { option ->
            val selected = defaultSort.sortOption == option

            ShimoriChip(
                onClick = {},
                modifier = Modifier
                    .shimoriPlaceholder(true)
                    .height(32.dp),
                text = LocalShimoriTextCreator.current.listSortText(type, option),
                selected = selected,
                icon = {
                    if (selected) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_up),
                            contentDescription = null
                        )
                    }
                }
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
    }
}

@Composable
private fun LoadingItem() {
    Box(
        modifier = Modifier.padding(PaddingValues(horizontal = 16.dp, vertical = 12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.listPosterHeight),
        ) {
            Box(
                modifier = Modifier
                    .shimoriPlaceholder(true)
                    .height(MaterialTheme.dimens.listPosterHeight)
                    .width(MaterialTheme.dimens.listPosterWidth)
            )

            Spacer(Modifier.width(16.dp))

            Column {
                Box(
                    modifier = Modifier
                        .shimoriPlaceholder(
                            visible = true,
                            shape = ShimoriSmallestRoundedCornerShape
                        )
                        .fillMaxWidth()
                        .height(66.dp)
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier.fillMaxSize()
                ) {
                    repeat(2) {
                        Box(
                            modifier = Modifier
                                .shimoriPlaceholder(
                                    visible = true,
                                    shape = ShimoriBiggestRoundedCornerShape
                                )
                                .clip(ShimoriBiggestRoundedCornerShape)
                                .size(32.dp)
                        )
                    }
                }
            }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenLayout(
    snackbarHostState: SnackbarHostState,
    message: UiMessage?,
    onChangeList: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ScaffoldExtended(
        snackbarHost = {
            ShimoriSnackbar(
                hostState = snackbarHostState,
                modifier = Modifier
                    .fillMaxWidth(),
                icon = {
                    val image = message?.image
                    val imageRes = message?.imageRes
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
                    } else if (imageRes != null) {
                        if (imageRes == ImageID.Tip) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_tip),
                                contentDescription = null
                            )
                        }
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
        floatingActionButtonPosition = com.gnoemes.shimori.common.ui.components.FabPosition.Center,
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