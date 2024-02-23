package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.lists.ListsRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@Inject
class ObservePinsExist(
    private val repository: ListsRepository
) : SubjectInteractor<Unit, Boolean>() {
    override fun create(params: Unit): Flow<Boolean> = repository.observePinsExist()
}