package com.gnoemes.shimori.lists.tabs.page

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.gnoemes.shimori.common.compose.AnimeListCard
import com.gnoemes.shimori.common.compose.MangaListCard
import com.gnoemes.shimori.common.compose.RanobeListCard
import com.gnoemes.shimori.common.extensions.rememberFlowWithLifecycle
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.rememberInsetsPaddingValues
import dagger.hilt.android.EntryPointAccessors


@Composable
fun AnimeListPage(
    status: RateStatus
) {
    AnimeListStatusPage(viewModel = viewModel(factory = animePageViewModel(status = status), key = "anime-$status"))
}

@Composable
fun MangaListPage(
    status: RateStatus
) {
    MangaListStatusPage(viewModel = viewModel(factory = mangaPageViewModel(status = status), key = "manga-$status"))
}

@Composable
fun RanobeListPage(
    status: RateStatus
) {
    RanobeListStatusPage(viewModel = viewModel(factory = ranobePageViewModel(status = status), key = "ranobe-$status"))
}

@Composable
private fun animePageViewModel(status: RateStatus): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            AnimeListPageViewModel.ViewModelFactoryProvider::class.java
    ).animePageFactory()

    return AnimeListPageViewModel.provideFactory(factory, status)
}

@Composable
private fun mangaPageViewModel(status: RateStatus): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            MangaListPageViewModel.ViewModelFactoryProvider::class.java
    ).mangaPageFactory()

    return MangaListPageViewModel.provideFactory(factory, status)
}

@Composable
private fun ranobePageViewModel(status: RateStatus): ViewModelProvider.Factory {
    val factory = EntryPointAccessors.fromActivity(
            LocalContext.current as Activity,
            RanobeListPageViewModel.ViewModelFactoryProvider::class.java
    ).ranobePageFactory()

    return RanobeListPageViewModel.provideFactory(factory, status)
}

@Composable
private fun AnimeListStatusPage(
    viewModel: AnimeListPageViewModel
) {
    val list = rememberFlowWithLifecycle(viewModel.list, key = "anime").collectAsLazyPagingItems()

    val submit = { action: ListPageAction -> viewModel.submitAction(action) }

    val onCoverLongCLick =
        { id: Long, type: RateTargetType -> submit(ListPageAction.TogglePin(id, type)) }

    PagingPage(list, onCoverLongCLick)
}

@Composable
private fun MangaListStatusPage(
    viewModel: MangaListPageViewModel
) {
    val list = rememberFlowWithLifecycle(viewModel.list, key = "manga").collectAsLazyPagingItems()

    val submit = { action: ListPageAction -> viewModel.submitAction(action) }

    val onCoverLongCLick =
        { id: Long, type: RateTargetType -> submit(ListPageAction.TogglePin(id, type)) }

    PagingPage(list, onCoverLongCLick)
}

@Composable
private fun RanobeListStatusPage(
    viewModel: RanobeListPageViewModel
) {
    val list = rememberFlowWithLifecycle(viewModel.list, key = "ranobe").collectAsLazyPagingItems()

    val submit = { action: ListPageAction -> viewModel.submitAction(action) }

    val onCoverLongCLick =
        { id: Long, type: RateTargetType -> submit(ListPageAction.TogglePin(id, type)) }

    PagingPage(list, onCoverLongCLick)
}

@Composable
internal fun PagingPage(
    list: LazyPagingItems<out EntityWithRate<out ShimoriEntity>>,
    onCoverLongCLick: (Long, RateTargetType) -> Unit
) {

    //toolbar height + sorts + tabs + first item spacing
    val top = 178.dp
    Page(
            contentPadding = rememberInsetsPaddingValues(
                    insets = LocalWindowInsets.current.systemBars,
                    applyTop = true,
                    additionalTop = top,
                    additionalBottom = 56.dp + 24.dp
            )
    ) {
        items(list) { item ->
            if (item != null) {
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
}

@Composable
internal fun Page(
    contentPadding: PaddingValues,
    items: LazyListScope.() -> Unit
) {
    LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = contentPadding,
            modifier = Modifier.fillMaxSize(),
            content = items
    )
}