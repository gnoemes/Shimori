package com.gnoemes.shimori.lists.edit

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.edit.components.BottomBar
import com.gnoemes.shimori.lists.edit.components.Note
import com.gnoemes.shimori.lists.edit.components.ProgressBoxes
import com.gnoemes.shimori.lists.edit.components.Rating
import com.gnoemes.shimori.lists.edit.components.StatusSelector

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
internal class TrackEditScreen(
    private val args: FeatureScreen.TrackEdit
) : Screen() {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<FeatureScreen.TrackEdit, TrackEditScreenModel>(
            arg = args
        )
        val navigator = LocalNavigator.currentOrThrow
        val navigateUp: () -> Unit = { navigator.pop() }

        val state by screenModel.state.collectAsState()

        LaunchedEffect(screenModel) {
            screenModel.uiEvents.collect {
                when (it) {
                    UiEvents.NavigateUp -> navigateUp()
                }
            }
        }

        TrackEdit(
            inputState = state.inputState,
            titleName = with(LocalShimoriTextCreator.current) {
                state.title?.let(::name).orEmpty()
            },
            status = state.status,
            anons = state.title?.status == TitleStatus.ANONS,
            progress = state.progress,
            size = state.title?.size,
            rewatches = state.rewatches,
            score = state.score,
            comment = state.comment,
            type = state.type,
            newTrack = state.newTrack,
            pinned = state.pinned,
            navigateUp = navigateUp,
            onStatusChanged = screenModel::onStatusChanged,
            onProgressChanged = screenModel::onProgressChanged,
            onRewatchesChanged = screenModel::onRewatchesChanged,
            onScoreChanged = screenModel::onScoreChanged,
            onProgressEdit = screenModel::onProgressInput,
            onRewatchesEdit = screenModel::onRewatchingInput,
            onCommentEdit = screenModel::onCommentInput,
            onCommentChanged = screenModel::onCommentChanged,
            onDefaultInputState = screenModel::onNoneInput,
            onDelete = screenModel::delete,
            onSave = screenModel::createOrUpdate,
            onTogglePin = screenModel::togglePin,
        )
    }

    @Composable
    private fun TrackEdit(
        inputState: TrackEditInputState,
        titleName: String,
        status: TrackStatus,
        anons: Boolean,
        progress: Int,
        size: Int?,
        rewatches: Int,
        score: Int?,
        comment: String?,
        type: TrackTargetType,
        newTrack: Boolean,
        pinned: Boolean,
        navigateUp: () -> Unit,
        onStatusChanged: (TrackStatus) -> Unit,
        onProgressChanged: (Int) -> Unit,
        onRewatchesChanged: (Int) -> Unit,
        onScoreChanged: (Int?) -> Unit,
        onProgressEdit: () -> Unit,
        onRewatchesEdit: () -> Unit,
        onCommentEdit: () -> Unit,
        onDefaultInputState: () -> Unit,
        onCommentChanged: (String?) -> Unit,
        onDelete: () -> Unit,
        onSave: () -> Unit,
        onTogglePin: () -> Unit,
    ) {
        Scaffold(
            topBar = {
                ShimoriSecondaryToolbar(
                    navigateUp = navigateUp,
                    title = titleName,
                    subTitle = stringResource(id = R.string.progress),
                )
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = inputState == TrackEditInputState.None,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column {
                        BottomBar(
                            modifier = Modifier
                                .height(84.dp),
                            newTrack = newTrack,
                            pinned = pinned,
                            onDelete = onDelete,
                            onSave = onSave,
                            onTogglePin = onTogglePin
                        )
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }
            },
            contentWindowInsets = WindowInsets.ime
        ) {
            val layoutDir = LocalLayoutDirection.current
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = it.calculateLeftPadding(layoutDir),
                        end = it.calculateRightPadding(layoutDir),
                        bottom = it.calculateBottomPadding(),
                    )
                    .consumeWindowInsets(it)
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(it.calculateTopPadding()))

                ScreenLayout(
                    inputState,
                    type,
                    status,
                    anons,
                    progress,
                    size,
                    rewatches,
                    score,
                    comment,
                    onStatusChanged,
                    onProgressEdit,
                    onProgressChanged,
                    onRewatchesEdit,
                    onRewatchesChanged,
                    onScoreChanged,
                    onCommentChanged,
                    onCommentEdit,
                    onDefaultInputState,
                )
            }
        }
    }

    @Composable
    private fun ScreenLayout(
        inputState: TrackEditInputState,
        type: TrackTargetType,
        status: TrackStatus,
        anons: Boolean,
        progress: Int,
        size: Int?,
        rewatches: Int,
        score: Int?,
        comment: String?,
        onStatusChanged: (TrackStatus) -> Unit,
        onProgressEdit: () -> Unit,
        onProgressChanged: (Int) -> Unit,
        onRewatchesEdit: () -> Unit,
        onRewatchesChanged: (Int) -> Unit,
        onScoreChanged: (Int?) -> Unit,
        onCommentChanged: (String?) -> Unit,
        onCommentEdit: () -> Unit,
        onDefaultInputState: () -> Unit,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            StatusSelector(
                type = type,
                selectedStatus = status,
                onStatusChanged = onStatusChanged
            )

            Rating(
                score = score,
                onScoreChanged = onScoreChanged
            )

            ProgressBoxes(
                inputState = inputState,
                anons = anons,
                progress = progress,
                size = size,
                onProgressChanged = onProgressChanged,
                onProgressEdit = onProgressEdit,
                rewatches = rewatches,
                onRewatchesChanged = onRewatchesChanged,
                onRewatchesEdit = onRewatchesEdit,
                onDefaultInputState = onDefaultInputState
            )

            Note(
                inputState = inputState,
                comment = comment,
                onCommentChanged = onCommentChanged,
                onCommentEdit = onCommentEdit,
                onDefaultInputState = onDefaultInputState,
            )

            Spacer(
                modifier = Modifier
                    .height(36.dp)
                    .navigationBarsPadding()
            )
        }
    }
}