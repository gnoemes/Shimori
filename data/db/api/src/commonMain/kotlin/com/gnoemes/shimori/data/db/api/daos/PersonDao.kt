package com.gnoemes.shimori.data.db.api.daos

import androidx.paging.PagingSource
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.track.TrackTargetType
import kotlinx.coroutines.flow.Flow

abstract class PersonDao : EntityDao<Person> {
    abstract fun queryAll(): List<Person>

    abstract fun queryById(id: Long): Person?

    abstract fun observeById(id: Long): Flow<Person?>

    abstract fun observeTitlePersonsCount(
        targetId: Long,
        targetType: TrackTargetType
    ): Flow<Int>

    abstract fun observeTitlePersons(
        targetId: Long,
        targetType: TrackTargetType,
    ): PagingSource<Int, Person>

}