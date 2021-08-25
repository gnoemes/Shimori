package com.gnoemes.shimori.lists.tabs.page.pinned

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.compose.AnimeListCard
import com.gnoemes.shimori.common.compose.MangaListCard
import com.gnoemes.shimori.common.compose.RanobeListCard
import com.gnoemes.shimori.lists.tabs.page.ListPageAction
import com.gnoemes.shimori.lists.tabs.page.Page
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues

@Composable
internal fun ListPinnedPage() {
    ListPinnedPage(viewModel = hiltViewModel())
}

@Composable
private fun ListPinnedPage(viewModel: ListPinnedPageViewModel) {

    val viewState by viewModel.state.collectAsState()

    val submit = { action: ListPageAction -> viewModel.submitAction(action) }

    val onCoverLongCLick =
        { id: Long, type: RateTargetType -> submit(ListPageAction.TogglePin(id, type)) }

    ListPage(
            list = viewState.list,
            onCoverLongCLick = onCoverLongCLick
    )
}

@Composable
private fun ListPage(
    list: List<EntityWithRate<out ShimoriEntity>>,
    onCoverLongCLick: (Long, RateTargetType) -> Unit
) {
    Page(
            contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = false,
                    additionalTop = 24.dp,
                    additionalBottom = 56.dp + 24.dp
            )
    ) {
        items(list) { item ->
            when (item) {
                is AnimeWithRate -> {
                    AnimeListCard(
                            anime = item,
                            onCoverLongClick = { item.entity.shikimoriId?.let { onCoverLongCLick(it, RateTargetType.ANIME) } }
                    )
                }
                is MangaWithRate -> {
                    MangaListCard(
                            manga = item,
                            onCoverLongClick = { item.entity.shikimoriId?.let { onCoverLongCLick(it, RateTargetType.MANGA) } }
                    )
                }
                is RanobeWithRate -> {
                    RanobeListCard(
                            ranobe = item,
                            onCoverLongClick = { item.entity.shikimoriId?.let { onCoverLongCLick(it, RateTargetType.RANOBE) } }
                    )
                }
            }
        }
    }
}
