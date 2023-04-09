package com.gnoemes.shimori.lists.edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.components.BottomSheetThumb
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.track.TrackStatus
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.lists.edit.components.BottomBar
import com.gnoemes.shimori.lists.edit.components.CommentInputState
import com.gnoemes.shimori.lists.edit.components.DefaultInputState
import com.gnoemes.shimori.lists.edit.components.ProgressInputState
import com.gnoemes.shimori.lists.edit.components.RewatchingInputState
import com.gnoemes.shimori.lists.edit.components.SheetLayout
import com.gnoemes.shimori.lists.edit.components.Title

internal class TrackEditScreen(
    private val args: FeatureScreen.TrackEdit
) : Screen() {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<FeatureScreen.TrackEdit, TrackEditScreenModel>(
            arg = args
        )
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val navigateUp = { bottomSheetNavigator.hide() }

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
            image = state.title?.image,
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
        image: ShimoriImage?,
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

        //TODO fix sticky buttons
        LocalBottomSheetNavigator.current.isVisible
        val offset = remember { mutableStateOf(0) }
        val bottomSheetOffset = remember { mutableStateOf(0f) }

        SheetLayout(
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding(),
            offset = offset,
            bottomSheetOffset = bottomSheetOffset,
            bottomBar = {
                if (inputState == TrackEditInputState.None) {
                    BottomBar(
                        modifier = Modifier
                            .height(84.dp)
                            .align(Alignment.BottomCenter)
                            .offset { IntOffset(0, offset.value) },
                        newTrack = newTrack,
                        pinned = pinned,
                        onDelete = onDelete,
                        onSave = onSave,
                        onTogglePin = onTogglePin
                    )
                }
            }
        ) {
            Column {
                BottomSheetThumb()

                val title = when (inputState) {
                    TrackEditInputState.Progress -> stringResource(id = R.string.progress)
                    TrackEditInputState.Rewatching -> stringResource(id = R.string.re_watches)
                    TrackEditInputState.Comment -> stringResource(id = R.string.note)
                    else -> titleName
                }

                var progressState by remember(progress) { mutableStateOf(progress) }

                val progressText = when {
                    inputState != TrackEditInputState.Progress -> null
                    progressState == size -> null
                    else -> stringResource(
                        id = R.string.left_format,
                        size?.let { it - progressState } ?: "?"
                    )
                }

                Title(
                    image = image,
                    text = title,
                    progressText = progressText
                )

                Spacer(modifier = Modifier.height(24.dp))

//            AnimatedContent(inputState) { state ->
                when (inputState) {
                    TrackEditInputState.Progress -> ProgressInputState(
                        progress = progressState,
                        progressDefault = progress,
                        size = size,
                        onProgressChanged = onProgressChanged,
                        onDefaultInputState = onDefaultInputState,
                        onChangedLocal = { progressState = it }
                    )

                    TrackEditInputState.Rewatching -> RewatchingInputState(
                        rewatches = rewatches,
                        onRewatchesChanged = onRewatchesChanged,
                        onDefaultInputState = onDefaultInputState
                    )

                    TrackEditInputState.Comment -> CommentInputState(
                        comment = comment,
                        onCommentChanged = onCommentChanged,
                        onDefaultInputState = onDefaultInputState
                    )

                    else -> DefaultInputState(
                        titleName = titleName,
                        type = type,
                        status = status,
                        anons = anons,
                        progress = progress,
                        size = size,
                        rewatches = rewatches,
                        score = score,
                        comment = comment,
                        onStatusChanged = onStatusChanged,
                        onProgressEdit = onProgressEdit,
                        onProgressChanged = onProgressChanged,
                        onRewatchesEdit = onRewatchesEdit,
                        onRewatchesChanged = onRewatchesChanged,
                        onScoreChanged = onScoreChanged,
                        onCommentEdit = onCommentEdit,
                    )
                }
//            }
            }
        }
    }
}