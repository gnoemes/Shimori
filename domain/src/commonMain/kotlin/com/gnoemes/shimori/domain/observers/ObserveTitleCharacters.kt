package com.gnoemes.shimori.domain.observers

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.data.characters.CharacterWithRole
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.PagingInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObserveTitleCharacters(
    private val repository: CharacterRepository
) : PagingInteractor<ObserveTitleCharacters.Params, CharacterWithRole>() {

    override fun create(params: Params): Flow<PagingData<CharacterWithRole>> {
        return Pager(
            config = params.pagingConfig,
            pagingSourceFactory = {
                repository.observeTitleCharacters(params.id, params.type, params.search)
            }
        ).flow
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        val search : String?,
        override val pagingConfig: PagingConfig
    ) : PagingParams<CharacterWithRole>
}