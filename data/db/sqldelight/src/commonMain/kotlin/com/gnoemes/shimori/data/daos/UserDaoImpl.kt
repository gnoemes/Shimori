package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.UserDao
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.data.util.UserDAO
import com.gnoemes.shimori.data.util.UserMapper
import com.gnoemes.shimori.data.util.queryMeShortToUserShortMapper
import com.gnoemes.shimori.data.util.userToUserShortMapper
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = UserDao::class)
class UserDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : UserDao, SqlDelightEntityDao<User> {

    override fun insert(entity: User): Long {
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

        return db.userQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: User) {
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

    override fun delete(entity: User) {
        db.userQueries.deleteById(entity.id)
    }

    override fun deleteMe(sourceId: Long) {
        db.userQueries.deleteMe(sourceId)
    }

    override fun observeMeShort(sourceId: Long): Flow<UserShort?> {
        return db.userQueries.queryMe(sourceId)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map(userToUserShortMapper::map)
            .flowOn(dispatchers.io)
    }

    override fun queryMe(sourceId: Long): User? {
        return db.userQueries.queryMe(sourceId).executeAsOneOrNull()?.let { UserMapper.map(it) }
    }

    override fun queryMeShort(sourceId: Long): UserShort? {
        return db.userQueries.queryMeShort(sourceId).executeAsOneOrNull()?.let {
            queryMeShortToUserShortMapper.map(it)
        }
    }
}