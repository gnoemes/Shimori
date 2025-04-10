package com.gnoemes.shimori.domain.interactors.tracks

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.events.TrackUiEvents
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class AddTrackProgress(
    private val createOrUpdateTrack: CreateOrUpdateTrack,
    private val calculateTrackStatus: CalculateTrackStatus,
    private val calculateTrackProgress: CalculateTrackProgress,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<AddTrackProgress.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            val track = params.track
            val newProgress = calculateTrackProgress(
                CalculateTrackProgress.Params(
                    params.changeBy,
                    track,
                    title = null
                )
            ).getOrThrow()

            var newTrack = track.copy(progress = newProgress)
            //calculate new status based on new progress & settings
            val calculatedStatus = calculateTrackStatus(
                CalculateTrackStatus.Params(newTrack)
            ).getOrThrow()

            if (calculatedStatus.openEdit) {
                //update track with old status and new progress
                createOrUpdateTrack.invoke(
                    CreateOrUpdateTrack.Params(newTrack)
                )

                //send event to open EditTrack screen with calculated status
                EventBus.publish(
                    TrackUiEvents.OpenEdit(
                        targetId = newTrack.targetId,
                        targetType = newTrack.targetType,
                        predefinedStatus = calculatedStatus.status
                    )
                )
            } else {
                newTrack = newTrack.copy(status = calculatedStatus.status)

                createOrUpdateTrack.invoke(
                    CreateOrUpdateTrack.Params(newTrack)
                )
            }
        }
    }

    data class Params(
        val changeBy: Int,
        val track: Track
    )
}