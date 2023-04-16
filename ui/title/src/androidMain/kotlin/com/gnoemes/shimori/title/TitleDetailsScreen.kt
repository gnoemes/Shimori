package com.gnoemes.shimori.title

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gnoemes.shimori.common.ui.api.OptionalContent
import com.gnoemes.shimori.common.ui.components.ScaffoldExtended
import com.gnoemes.shimori.common.ui.components.ShimoriSecondaryToolbar
import com.gnoemes.shimori.common.ui.itemSpacer
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.TitleWithTrackEntity
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.title.components.About
import com.gnoemes.shimori.title.components.Characters
import com.gnoemes.shimori.title.components.Screenshots
import com.gnoemes.shimori.title.components.TitleHeader
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
                title = state.title,
                characters = state.characters,
                videos = state.videos,
                screenshots = state.screenshots,
                openTrackEdit = { id, type, markComplete ->
                    navigator.push(
                        screen(
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
                openCharacterList = {},
                openScreenshots = {}
            )
        }

    }

    @Composable
    private fun TitleContent(
        modifier: Modifier,
        state: LazyListState,
        title: TitleWithTrackEntity?,
        characters: OptionalContent<List<Character>?>,
        videos: OptionalContent<List<AnimeVideo>?>,
        screenshots: OptionalContent<List<AnimeScreenshot>?>,
        openTrackEdit: (Long, TrackTargetType, Boolean) -> Unit,
        openCharacterDetails: (id: Long) -> Unit,
        openCharacterList: () -> Unit,
        openScreenshots : () -> Unit,
    ) {

        LazyColumn(
            modifier = modifier,
            state = state,
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item {
                TitleHeader(
                    title,
                    openTrackEdit
                )
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

            if (title == null || !title.entity.description.isNullOrBlank() || !title.entity.genres.isNullOrEmpty()) {
                item {
                    About(title = title?.entity)
                }

                itemSpacer(32.dp)
            }

            if (!screenshots.loaded || !screenshots.content.isNullOrEmpty()) {
                item {
                    Screenshots(
                        screenshots,
                        openScreenshots
                    )
                }

                itemSpacer(32.dp)
            }

            if (!videos.loaded || !videos.content.isNullOrEmpty()) {
                item {
                    Trailers(videos)
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