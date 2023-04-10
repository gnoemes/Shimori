package com.gnoemes.shimori.title

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gnoemes.shimori.common.ui.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.common.ui.components.Background
import com.gnoemes.shimori.common.ui.components.DescriptionFormat
import com.gnoemes.shimori.common.ui.components.ScaffoldExtended
import com.gnoemes.shimori.common.ui.components.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.ui.components.TitleDescription
import com.gnoemes.shimori.common.ui.itemSpacer
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.common.ui.statusBarHeight
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.title.components.About
import com.gnoemes.shimori.title.components.BackDropImage
import com.gnoemes.shimori.title.components.Characters
import com.gnoemes.shimori.title.components.TitleActions
import com.gnoemes.shimori.title.components.TitleProperties
import com.gnoemes.shimori.title.components.Trailers

@OptIn(ExperimentalMaterial3Api::class)
internal class TitleDetailsScreen(
    private val args: FeatureScreen.TitleDetails
) : Screen() {

    @Composable
    override fun Content() {
        val screenModel =
            rememberScreenModel<FeatureScreen.TitleDetails, TitleDetailsScreenModel>(arg = args)

        val state by screenModel.state.collectAsState()

        val navigator = LocalNavigator.currentOrThrow
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        val scrollState = rememberLazyListState()

        Background {
            val title = state.title ?: return@Background

            ScaffoldExtended(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    //TODO scroll condition
                    val toolbarTitle = ""

                    ShimoriSecondaryToolbar(
                        navigateUp = { navigator.pop() },
                        title = toolbarTitle,
                        containerColor = Color.Transparent,
                        actions = {
//                    TODO actions
                        })
                }
            ) {
                TitleContent(
                    modifier = Modifier.fillMaxSize(),
                    state = scrollState,
                    title = title,
                    characters = state.characters,
                    videos = state.videos,
                    screenshots = state.screenshots,
                    openTrackEdit = { id, type, markComplete ->
                        navigator.push(
                            ScreenRegistry.get(
                                FeatureScreen.TrackEdit(
                                    id,
                                    type,
                                    markComplete,
                                    false
                                )
                            )
                        )
                    },
                    openCharacterDetails = { },
                    openCharacterList = {}
                )
            }
        }
    }

    @Composable
    private fun TitleContent(
        modifier: Modifier,
        state: LazyListState,
        title: TitleWithTrackEntity,
        characters: OptionalContent<List<Character>?>,
        videos: OptionalContent<List<AnimeVideo>?>,
        screenshots: OptionalContent<List<AnimeScreenshot>?>,
        openTrackEdit: (Long, TrackTargetType, Boolean) -> Unit,
        openCharacterDetails: (id: Long) -> Unit,
        openCharacterList: () -> Unit
    ) {
        val textCreator = LocalShimoriTextCreator.current

        LazyColumn(
            modifier = modifier, state = state, contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                Box {
                    BackDropImage(title.entity.image)
                    Column {
                        Spacer(modifier = Modifier.statusBarHeight(additional = 128.dp))
                        if (title.entity.isOngoing || (title.entity.rating ?: 0.0) > 0) {
                            TitleDescription(
                                title = title.entity,
                                format = DescriptionFormat.Title,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(16.dp)
                            )
                        } else {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = textCreator.name(title = title.entity),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        TitleProperties(title.entity)
                        Spacer(modifier = Modifier.height(32.dp))
                        TitleActions(
                            title = title,
                            openListsEdit = openTrackEdit,
                            onFavoriteClick = { TODO() },
                            onShareClicked = { TODO() })
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }


            if (!characters.loaded || !characters.content.isNullOrEmpty()) {
                item {
                    Characters(
                        characters = characters,
                        openCharacterDetails = openCharacterDetails,
                        openCharacterList = openCharacterList,
                    )
                }

                itemSpacer(32.dp)
            }

            if (!title.entity.description.isNullOrBlank() || !title.entity.genres.isNullOrEmpty()) {
                item {
                    About(title = title.entity)
                }

                itemSpacer(32.dp)
            }

            if (!videos.loaded
                || !videos.content.isNullOrEmpty()
                || !screenshots.loaded
                || !screenshots.content.isNullOrEmpty()
            ) {
                item {
                    Trailers(
                        videos
                    )
                }

                itemSpacer(32.dp)
            }

            itemSpacer(32.dp)


            item {
                Spacer(modifier = Modifier.height(MaterialTheme.dimens.bottomBarHeight + 24.dp))
            }
        }
    }
}