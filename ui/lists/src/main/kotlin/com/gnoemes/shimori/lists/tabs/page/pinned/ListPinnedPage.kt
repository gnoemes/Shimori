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

    ListPage(
            list = viewState.list,
            onCoverLongCLick = { id, type -> submit(ListPageAction.TogglePin(id, type)) },
            onEditClick = { id, type -> submit(ListPageAction.Edit(id, type)) },
            onIncrementClick = { id, type -> TODO("add increment") },
            onIncrementHold = { id, type -> TODO("add increment tool") },
    )
}

@Composable
private fun ListPage(
    list: List<EntityWithRate<out ShimoriEntity>>,
    onCoverLongCLick: (Long, RateTargetType) -> Unit,
    onEditClick: (Long, RateTargetType) -> Unit,
    onIncrementClick: (Long, RateTargetType) -> Unit,
    onIncrementHold: (Long, RateTargetType) -> Unit,
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
                    val shikimoriId = item.entity.shikimoriId
                    val type = RateTargetType.ANIME

                    AnimeListCard(
                            anime = item,
                            onCoverLongClick = { shikimoriId?.let { onCoverLongCLick(it, type) } },
                            onEditClick = { shikimoriId?.let { onEditClick(it, type) } },
                            onIncrementClick = { shikimoriId?.let { onIncrementClick(it, type) } },
                            onIncrementHold = { shikimoriId?.let { onIncrementHold(it, type) } },
                    )
                }
                is MangaWithRate -> {
                    val shikimoriId = item.entity.shikimoriId
                    val type = RateTargetType.MANGA

                    MangaListCard(
                            manga = item,
                            onCoverLongClick = { shikimoriId?.let { onCoverLongCLick(it, type) } },
                            onEditClick = { shikimoriId?.let { onEditClick(it, type) } },
                            onIncrementClick = { shikimoriId?.let { onIncrementClick(it, type) } },
                            onIncrementHold = { shikimoriId?.let { onIncrementHold(it, type) } },
                    )
                }
                is RanobeWithRate -> {
                    val shikimoriId = item.entity.shikimoriId
                    val type = RateTargetType.RANOBE

                    RanobeListCard(
                            ranobe = item,
                            onCoverLongClick = { shikimoriId?.let { onCoverLongCLick(it, type) } },
                            onEditClick = { shikimoriId?.let { onEditClick(it, type) } },
                            onIncrementClick = { shikimoriId?.let { onIncrementClick(it, type) } },
                            onIncrementHold = { shikimoriId?.let { onIncrementHold(it, type) } },
                    )
                }
            }
        }
    }
}
