package com.gnoemes.shimori.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleCharacters(
    private val repository: CharacterRepository
) : PagingInteractor<ObserveTitleCharacters.Params, Character>() {

    override fun create(params: Params): Flow<PagingData<Character>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                repository.observeByTitle(params.id, params.type)
            }
        ).flow
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        override val pagingConfig: PagingConfig
    ) : PagingParams<Character>
}