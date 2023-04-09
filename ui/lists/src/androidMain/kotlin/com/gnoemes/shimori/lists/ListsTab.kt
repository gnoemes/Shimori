package com.gnoemes.shimori.lists

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.gnoemes.shimori.common.ui.LocalShimoriDimensions
import com.gnoemes.shimori.common.ui.components.RootScreenLayout
import com.gnoemes.shimori.common.ui.components.ShimoriFAB
import com.gnoemes.shimori.common.ui.components.ShimoriSnackbar
import com.gnoemes.shimori.common.ui.components.TitleSnackIcon
import com.gnoemes.shimori.common.ui.components.rememberSnackbarHostState
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Tab
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.lists.components.ListPage
import com.gnoemes.shimori.lists.components.ListsEmpty
import com.gnoemes.shimori.lists.components.LoadingCurrentStatus
import com.gnoemes.shimori.lists.components.LoadingItem
import com.gnoemes.shimori.lists.components.LoadingSort
import com.gnoemes.shimori.lists.components.ScreenLayout
import com.gnoemes.shimori.lists.page.ListPageScreenModel
import com.gnoemes.shimori.lists.sort.ListSortScreenModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
internal object ListsTab : Tab {
    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 0u,
                title = stringResource(R.string.lists_title),
                icon = painterResource(id = R.drawable.ic_bookmark)
            )
        }


    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<ListsScreenModel>()
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val listMenuScreen = rememberScreen(FeatureScreen.ListMenu)

        val state by screenModel.state.collectAsState()

        val appBarState = rememberTopAppBarState()
        val pinBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
            state = appBarState
        )
        val scrollBehavior = remember { pinBehavior }

        val snackbarHostState = rememberSnackbarHostState()

        val message = (state as? ListScreenState.Data)?.uiMessage
        message?.let {
            LaunchedEffect(message) {
                val result =
                    snackbarHostState.showSnackbar(
                        message.message,
                        actionLabel = message.action,
                        duration = SnackbarDuration.Short
                    )

                if (result == SnackbarResult.ActionPerformed) {
                    screenModel.onMessageAction(message.id)
                }

                delay(100L)
                screenModel.onMessageShown(message.id)
            }
        }
        RootScreenLayout(
            scrollBehavior = scrollBehavior,
            snackBarHost = {
                ShimoriSnackbar(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .fillMaxWidth(),
                    icon = {
                        val image = message?.image
                        if (image != null) {
                            TitleSnackIcon(image)
                        }
                    })
            },
            floatingActionButton = {

                if (state !is ListScreenState.NoTracks) {
                    ShimoriFAB(
                        onClick = {
                            bottomSheetNavigator.show(listMenuScreen)
                        },
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
                }
            },
            content = { paddingValues ->
                when (state) {
                    ListScreenState.Loading -> LoadingState(paddingValues)
                    is ListScreenState.NoTracks, ListScreenState.NoPins -> {
                        EmptyState(
                            type = if (state == ListScreenState.NoPins) ListType.Pinned
                            else (state as ListScreenState.NoTracks).type,
                            onAnimeExplore = { /*TODO */ },
                            onMangaExplore = { /*TODO */ },
                            onRanobeExplore = { /*TODO */ },
                        )
                    }

                    is ListScreenState.Data -> DataState(
                        scrollBehavior = scrollBehavior,
                        snackbarHostState = snackbarHostState,
                        paddingValues = paddingValues
                    )
                }
            }
        )
    }

    @Composable
    private fun LoadingState(paddingValues: PaddingValues) {
        val bottomBarHeight = LocalShimoriDimensions.current.bottomBarHeight
        ScreenLayout {
            Column {
                Spacer(Modifier.height(paddingValues.calculateTopPadding() + 24.dp))
                LoadingSort()
                LoadingCurrentStatus()
                repeat(5) { LoadingItem() }
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        //bottomBar + FAB
                        .height(bottomBarHeight + 64.dp)
                )
            }
        }
    }

    @Composable
    private fun EmptyState(
        type: ListType,
        onAnimeExplore: () -> Unit,
        onMangaExplore: () -> Unit,
        onRanobeExplore: () -> Unit,
    ) {
        ListsEmpty(
            type = type,
            onAnimeExplore = onAnimeExplore,
            onMangaExplore = onMangaExplore,
            onRanobeExplore = onRanobeExplore
        )
    }

    @Composable
    private fun DataState(
        scrollBehavior: TopAppBarScrollBehavior,
        snackbarHostState: SnackbarHostState,
        paddingValues: PaddingValues
    ) {
        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val screenModel = rememberScreenModel<ListPageScreenModel>()
        val sortScreenModel = rememberScreenModel<ListSortScreenModel>()

        ListPage(
            screenModel = screenModel,
            sortScreenModel = sortScreenModel,
            paddingValues = paddingValues,
            scrollBehavior = scrollBehavior,
            snackbarHostState = snackbarHostState,
            onAnimeExplore = { /*TODO*/ },
            onMangaExplore = { /*TODO*/ },
            onRanobeExplore = { /*TODO*/ },
            openTrackEdit = { id, type, markComplete ->
                bottomSheetNavigator.show(
                    ScreenRegistry.get(
                        FeatureScreen.TrackEdit(
                            id,
                            type,
                            markComplete,
                            true
                        )
                    )
                )
            },
            openTitleDetails = { id, type ->
                navigator.push(ScreenRegistry.get(FeatureScreen.TitleDetails(id, type)))
            }
        )
    }
}