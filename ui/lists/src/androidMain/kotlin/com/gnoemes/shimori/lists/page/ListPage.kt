package com.gnoemes.shimori.lists.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.gnoemes.shimori.common.ui.*
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.components.*
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.common.ui.utils.ImageID
import com.gnoemes.shimori.common.ui.utils.shimoriViewModel
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.lists.INCREMENTATOR_MAX_PROGRESS
import com.gnoemes.shimori.lists.R
import com.gnoemes.shimori.lists.sort.ListSort
import kotlinx.coroutines.delay

@Composable
internal fun ListPage(
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    ListPage(
        viewModel = shimoriViewModel(),
        onChangeList = onChangeList,
        openSearch = openSearch,
        openUser = openUser,
        openListsEdit = openListsEdit,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
private fun ListPage(
    viewModel: ListPageViewModel,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    openListsEdit: (id: Long, type: RateTargetType) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val appBarState = rememberTopAppBarState()
    val scrollBehavior = remember {
        TopAppBarDefaults.pinnedScrollBehavior(
            state = appBarState
        )
    }
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
                snackbarHostState.showSnackbar(message.message, actionLabel = message.action)

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

    val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight

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
        type = state.type,
        status = state.status,
        user = state.user,
        message = state.message,
        snackbarHostState = snackbarHostState,
        scrollBehavior = scrollBehavior,
        onChangeList = onChangeList,
        openSearch = openSearch,
        openUser = openUser
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize(),
            state = listItems.rememberLazyListState()
        ) {
            itemSpacer(paddingValues.calculateTopPadding())
            item("sort") { ListSort() }

            items(
                items = listItems,
                key = { it.id }
            ) { entity ->
                if (entity != null) {
                    ListCard(
                        title = entity,
                        onCoverLongClick = { onTogglePin(entity) },
                        onEditClick = { onEditClick(entity) },
                        onIncrementClick = { onIncrementClick() },
                        onIncrementHold = { onIncrementHold(entity) })
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
        val incrementerTitle = state.incrementerTitle

        AnimatedVisibility(
            visible = incrementerTitle != null,
            enter = slideInHorizontally(
                initialOffsetX = { offset }
            ),
        ) {
            val rate = incrementerTitle?.rate
            if (incrementerTitle == null || rate == null) return@AnimatedVisibility

            var incrementerProgress by remember(incrementerTitle.rate) { mutableStateOf(rate.progress) }

            val localProgressUpdate = { value: Int -> incrementerProgress = value }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .noRippleClickable { onIncrementerProgress(incrementerProgress) }
            ) {
                Incrementer(
                    title = incrementerTitle,
                    rate = rate,
                    onProgressUpdated = localProgressUpdate
                )
            }
        }
    }
}

@Composable
private fun BoxScope.Incrementer(
    title: TitleWithRateEntity,
    rate: Rate,
    onProgressUpdated: (Int) -> Unit,
) {
    val incrementerHeight = 240.dp
    val incrementerHeightPx: Float
    val colorChangeHeights: ClosedRange<Float>

    with(LocalDensity.current) {
        incrementerHeightPx = incrementerHeight.toPx()
        colorChangeHeights = incrementerHeightPx - 48.dp.roundToPx()..incrementerHeightPx
    }

    //up to 50
    val maxProgress = remember(rate) {
        val size = title.entity.size
        val current = rate.progress
        size?.let {
            if (current + INCREMENTATOR_MAX_PROGRESS >= size) size - current
            else INCREMENTATOR_MAX_PROGRESS
        } ?: INCREMENTATOR_MAX_PROGRESS
    }

    //calculate height of single section
    val sectionHeightPx = remember(maxProgress) { incrementerHeightPx / maxProgress }

    var delta by remember { mutableStateOf(sectionHeightPx) }

    val state = rememberDraggableState { delta += it }
    val deltaCoerced = delta.coerceIn(0f, incrementerHeightPx)

    var currentSection by remember(rate) { mutableStateOf(-1) }

    //increment by 1 initially
    currentSection = if (currentSection == -1) 1
    else (deltaCoerced / sectionHeightPx).toInt()

    onProgressUpdated(rate.progress + currentSection)

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


        Image(
            painter = rememberAsyncImagePainter(
                ImageRequest
                    .Builder(LocalContext.current)
                    .data(title.entity.image)
                    .apply {
                        crossfade(true)
                    }.build()
            ),
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
    type: ListType,
    status: RateStatus,
    user: UserShort?,
    message: UiMessage?,
    snackbarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    onChangeList: () -> Unit,
    openSearch: () -> Unit,
    openUser: () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    ScaffoldExtended(
        topBar = {
            val title = when (type) {
                ListType.Pinned -> null
                else -> type.rateType
            }?.let { type ->
                LocalShimoriTextCreator.current.rateStatusText(type, status)
            } ?: LocalShimoriRateUtil.current.listTypeName(type)

            ShimoriMainToolbar(
                modifier = Modifier,
                title = title,
                user = user,
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
                    val imageRes = message?.imageRes
                    if (image != null) {
                        Image(
                            painter = rememberAsyncImagePainter(image),
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