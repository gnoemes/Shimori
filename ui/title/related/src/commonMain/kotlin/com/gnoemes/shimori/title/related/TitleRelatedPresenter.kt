package com.gnoemes.shimori.title.related

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.paging.PagingConfig
import app.cash.paging.PagingData
import app.cash.paging.compose.collectAsLazyPagingItems
import com.gnoemes.shimori.base.inject.UiScope
import com.gnoemes.shimori.base.utils.launchOrThrow
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.compose.rememberRetainedCachedPagingFlow
import com.gnoemes.shimori.common.ui.wrapEventSink
import com.gnoemes.shimori.data.common.Related
import com.gnoemes.shimori.domain.interactors.UpdateTitleRelated
import com.gnoemes.shimori.domain.observers.ObserveTitleRelated
import com.gnoemes.shimori.domain.observers.ObserveTitleWithTrackEntity
import com.gnoemes.shimori.domain.onFailurePublishToBus
import com.gnoemes.shimori.screens.TitleDetailsScreen
import com.gnoemes.shimori.screens.TitleRelatedScreen
import com.gnoemes.shimori.screens.TrackEditScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.retained.rememberRetained
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.mapNotNull
import me.tatarka.inject.annotations.Assisted
import me.tatarka.inject.annotations.Inject

@Inject
@CircuitInject(screen = TitleRelatedScreen::class, UiScope::class)
class TitleRelatedPresenter(
    @Assisted private val screen: TitleRelatedScreen,
    @Assisted private val navigator: Navigator,
    private val updateTitleRelated: Lazy<UpdateTitleRelated>,
    private val observeTitleRelated: Lazy<ObserveTitleRelated>,
    private val observeTitle: Lazy<ObserveTitleWithTrackEntity>,
) : Presenter<TitleRelatedUiState> {

    @Composable
    override fun present(): TitleRelatedUiState {
        val asContent = screen.asContent

        val title by observeTitle.value.flow
            .mapNotNull { it?.entity }
            .distinctUntilChangedBy { it.id }
            .collectAsState(null)

        val titleNameLocalized = LocalShimoriTextCreator.current.nullable {
            title?.name()
        } ?: ""

        val titleName by remember { derivedStateOf { titleNameLocalized } }
        val relatedUpdating by updateTitleRelated.value.inProgress.collectAsState(true)

        val retainedPagingInteractor =
            rememberRetained(screen.id, screen.type) { observeTitleRelated.value }
        val relatedFlow = remember(screen.id, screen.type) {
            retainedPagingInteractor.flow
                .filterIsInstance<PagingData<Related>>()
        }

        val related = relatedFlow
            .rememberRetainedCachedPagingFlow()
            .collectAsLazyPagingItems()

        if (!asContent) {
            LaunchedEffect(Unit) {
                observeTitle.value(
                    ObserveTitleWithTrackEntity.Params(screen.id, screen.type)
                )
            }
        }

        LaunchedEffect(Unit) {
            launchOrThrow {
                updateTitleRelated.value(
                    UpdateTitleRelated.Params.optionalUpdate(
                        screen.id,
                        screen.type
                    )
                ).onFailurePublishToBus()
            }

            observeTitleRelated.value(
                ObserveTitleRelated.Params(
                    screen.id,
                    screen.type,
                    PAGING_CONFIG
                )
            )
        }

        val eventSink: CoroutineScope.(TitleRelatedUiEvent) -> Unit = { event ->
            when (event) {
                TitleRelatedUiEvent.NavigateUp -> navigator.pop()
                is TitleRelatedUiEvent.OpenTitle -> {
                    navigator.goTo(TitleDetailsScreen(event.targetId, event.targetType))
                }

                is TitleRelatedUiEvent.OpenEdit -> {
                    navigator.goTo(
                        TrackEditScreen(
                            event.targetId,
                            event.targetType,
                            predefinedStatus = null
                        )
                    )
                }

            }
        }

        return TitleRelatedUiState(
            isList = asContent,
            related = related,
            eventSink = wrapEventSink(eventSink)
        )
    }

    private companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 20,
        )
    }
}