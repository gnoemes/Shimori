package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.UserDao
import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.UserDAO
import com.gnoemes.shimori.data.shared.UserMapper
import com.gnoemes.shimori.data.shared.queryMeShortToUserShortMapper
import com.gnoemes.shimori.data.shared.userToUserShortMapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class UserDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : UserDao() {

    override suspend fun insert(entity: User) {
        entity.let {
            db.userQueries.insert(
                remote_id = it.remoteId,
                source_id = it.sourceId,
                nickname = it.nickname,
                image_original = it.image?.original,
                image_preview = it.image?.preview,
                image_x96 = it.image?.x96,
                image_x48 = it.image?.x48,
                name = it.name,
                about = it.about,
                common_info = it.commonInfo,
                sex = it.sex,
                website = it.website,
                date_birth = it.dateBirth,
                locale = it.locale,
                full_years = it.fullYears,
                location = it.location,
                show_comments = it.showComments,
                friend = it.friend,
                ignored = it.ignored,
                banned = it.banned,
                last_online = it.lastOnlineAt,
                is_me = it.isMe
            )
        }
    }

    override suspend fun update(entity: User) {
        entity.let {
            db.userQueries.update(
                UserDAO(
                    id = it.id,
                    remote_id = it.remoteId,
                    source_id = it.sourceId,
                    nickname = it.nickname,
                    image_original = it.image?.original,
                    image_preview = it.image?.preview,
                    image_x96 = it.image?.x96,
                    image_x48 = it.image?.x48,
                    name = it.name,
                    about = it.about,
                    common_info = it.commonInfo,
                    sex = it.sex,
                    website = it.website,
                    date_birth = it.dateBirth,
                    locale = it.locale,
                    full_years = it.fullYears,
                    location = it.location,
                    show_comments = it.showComments,
                    friend = it.friend,
                    ignored = it.ignored,
                    banned = it.banned,
                    last_online = it.lastOnlineAt,
                    is_me = it.isMe
                )
            )
        }
    }

    override suspend fun delete(entity: User) {
        db.userQueries.deleteById(entity.id)
    }

    override suspend fun deleteMe(sourceId: Long) {
        db.userQueries.deleteMe(sourceId)
    }

    override fun observeMeShort(sourceId: Long): Flow<UserShort?> {
        return db.userQueries.queryMe(sourceId)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map(userToUserShortMapper::map)
            .flowOn(dispatchers.io)
    }

    override suspend fun queryMe(sourceId: Long): User? {
        return db.userQueries.queryMe(sourceId).executeAsOneOrNull()?.let { UserMapper.map(it) }
    }

    override suspend fun queryMeShort(sourceId: Long): UserShort? {
        return db.userQueries.queryMeShort(sourceId).executeAsOneOrNull()?.let {
            queryMeShortToUserShortMapper.map(it)
        }
    }
}