package com.gnoemes.shimori.title.trailers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.eventbus.EventBus
import com.gnoemes.shimori.data.eventbus.StateBus
import com.gnoemes.shimori.data.events.TitleUiEvents
import com.gnoemes.shimori.domain.observers.ObserveAnimeVideos
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.screens.TitleTrailersScreen
import com.gnoemes.shimori.screens.UrlScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TitleTrailersScreen::class, UiScope::class)
class TitleTrailersPresenter(
    @Assisted private val screen: TitleTrailersScreen,
    @Assisted private val navigator: Navigator,
    private val observeTitleTrailers: Lazy<ObserveAnimeVideos>,
    private val observeTitle: Lazy<ObserveTitleWithTrackEntity>,
    private val stateBus: Lazy<StateBus>,
) : Presenter<TitleTrailersUiState> {

    @Composable
    override fun present(): TitleTrailersUiState {
        val asContent = screen.asContent


        val title = if (!screen.asContent) observeTitle.value.flow
            .mapNotNull { it?.entity }
            .collectAsState(null)
        else mutableStateOf(null)

        val titleNameLocalized = LocalShimoriTextCreator.current.nullable {
            title.value?.name()
        } ?: ""

        val titleName by remember { derivedStateOf { titleNameLocalized } }
        val titleUpdating by stateBus.value.titleUpdating.observe.collectAsState(false)

        val trailers by observeTitleTrailers.value.flow.collectAsState(null)

        if (!asContent) {
            LaunchedEffect(Unit) {
                observeTitle.value(
                    ObserveTitleWithTrackEntity.Params(screen.id, screen.type)
                )
            }
        }

        LaunchedEffect(Unit) {
            observeTitleTrailers.value(
                ObserveAnimeVideos.Params(screen.id)
            )
        }


        val hideList = asContent && !titleUpdating && trailers != null && trailers.isNullOrEmpty()
        LaunchedEffect(hideList) {
            if (hideList) {
                EventBus.publish(TitleUiEvents.HideTrailers)
            }
        }

        val eventSink: CoroutineScope.(TitleTrailersUiEvent) -> Unit = { event ->
            when (event) {
                TitleTrailersUiEvent.NavigateUp -> navigator.pop()
                is TitleTrailersUiEvent.OpenTrailer -> {
                    val video = trailers?.find { it.id == event.id }
                    if (video != null && video.url.isNotEmpty()) {
                        navigator.goTo(UrlScreen(video.url))
                    }
                }
            }
        }

        return TitleTrailersUiState(
            asContent = asContent,
            titleName = titleName,
            trailers = trailers,
            eventSink = wrapEventSink(eventSink)
        )
    }
}