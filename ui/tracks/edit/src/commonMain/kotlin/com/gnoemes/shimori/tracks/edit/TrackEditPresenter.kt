package com.gnoemes.shimori.tracks.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.TrackUiEvents
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.domain.interactors.tracks.CalculateTrackProgress
import com.gnoemes.shimori.domain.interactors.tracks.CreateOrUpdateTrack
import com.gnoemes.shimori.domain.interactors.tracks.DeleteTrack
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.screens.TrackEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.collectAsRetainedState
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TrackEditScreen::class, UiScope::class)
class TrackEditPresenter(
    @Assisted private val screen: TrackEditScreen,
    @Assisted private val navigator: Navigator,
    private val observeTitleWithTrack: Lazy<ObserveTitleWithTrackEntity>,
    private val calculateTrackProgress: CalculateTrackProgress,
    private val createOrUpdateTrack: CreateOrUpdateTrack,
    private val deleteTrack: DeleteTrack,
) : Presenter<TrackEditUiState> {

    @Composable
    override fun present(): TrackEditUiState {
        val title by observeTitleWithTrack.value.flow.collectAsRetainedState(null)
        val trackExists by remember(title?.track) { mutableStateOf(title?.track != null) }
        val textCreator = LocalShimoriTextCreator.current

        val titleName = title?.entity?.let {
            textCreator {
                it.name()
            }
        } ?: ""


        var localTrack by remember(trackExists) {
            mutableStateOf(
                if (trackExists) {
                    //safe
                    val track = title!!.track!!

                    //set predefined status
                    track.copy(
                        status = screen.predefinedStatus ?: track.status
                    )
                } else Track(
                    targetId = screen.targetId,
                    targetType = screen.targetType,
                    status = screen.predefinedStatus ?: TrackStatus.WATCHING
                )
            )
        }

        val maxProgress by remember(title?.id) {
            derivedStateOf {
                title?.entity?.calculateProgressLimit() ?: 0
            }
        }

        val decrementEnabled by remember(localTrack.progress) {
            derivedStateOf {
                localTrack.progress > 0
            }
        }

        val incrementEnabled by remember(localTrack.progress, maxProgress) {
            derivedStateOf {
                localTrack.progress < maxProgress
            }
        }

        LaunchedEffect(Unit) {
            observeTitleWithTrack.value(
                ObserveTitleWithTrackEntity.Params(
                    screen.targetId,
                    screen.targetType
                )
            )
        }

        val eventSink: CoroutineScope.(TrackEditUiEvent) -> Unit = { event ->
            when (event) {
                is TrackEditUiEvent.ChangeNote -> {
                    localTrack = localTrack.copy(comment = event.newValue)
                }

                is TrackEditUiEvent.ChangeProgress -> {
                    localTrack = localTrack
                        .copy(progress = constrainProgress(event.newValue, maxProgress))
                }

                is TrackEditUiEvent.ChangeRepeats -> {
                    localTrack = localTrack.copy(reCounter = event.newValue ?: localTrack.reCounter)
                }

                is TrackEditUiEvent.ChangeScore -> {
                    localTrack = localTrack.copy(score = event.newValue)
                }

                is TrackEditUiEvent.ChangeStatus -> {
                    localTrack = localTrack.copy(status = event.newValue)
                }

                TrackEditUiEvent.DecrementProgress -> launchOrThrow {
                    calculateTrackProgress(
                        CalculateTrackProgress.Params(
                            -1,
                            localTrack,
                            title!!.entity
                        )
                    ).getOrNull()?.let { localTrack = localTrack.copy(progress = it) }
                }

                TrackEditUiEvent.IncrementProgress -> launchOrThrow {
                    calculateTrackProgress(
                        CalculateTrackProgress.Params(
                            1,
                            localTrack,
                            title!!.entity
                        )
                    ).getOrNull()?.let { localTrack = localTrack.copy(progress = it) }
                }

                TrackEditUiEvent.Delete -> launchOrThrow {
                    deleteTrack(DeleteTrack.Params(localTrack.id))
                        .onSuccess {
                            EventBus.publish(
                                TrackUiEvents.TrackDeleted(
                                    title?.entity?.image,
                                    localTrack
                                )
                            )
                            navigator.pop()
                        }
                        .onFailure { navigator.pop() }
                }

                TrackEditUiEvent.Save -> launchOrThrow {
                    createOrUpdateTrack(CreateOrUpdateTrack.Params(localTrack))
                        .onSuccess { navigator.pop() }
                        .onFailure { navigator.pop() }
                }

                TrackEditUiEvent.NavigateUp -> navigator.pop()
            }
        }

        return TrackEditUiState(
            isEdit = trackExists,
            titleName = titleName,
            type = localTrack.targetType,
            status = localTrack.status,
            score = localTrack.score,
            progress = localTrack.progress,
            maxProgress = maxProgress,
            repeats = localTrack.reCounter,
            decrementEnabled = decrementEnabled,
            incrementEnabled = incrementEnabled,
            note = localTrack.comment,
            eventSink = wrapEventSink(eventSink)
        )
    }

    private fun constrainProgress(progress: Int?, maxProgress: Int): Int {
        return when {
            progress == null || progress < 0 -> 0
            progress >= maxProgress -> maxProgress
            else -> progress
        }
    }

}