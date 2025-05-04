package com.gnoemes.shimori.source.shikimori

import com.apollographql.apollo.api.Query
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.shikimori.models.anime.CalendarResponse
import com.gnoemes.shimori.source.shikimori.models.anime.StudioResponse
import com.gnoemes.shimori.source.shikimori.models.common.ShikimoriTargetType
import com.gnoemes.shimori.source.shikimori.models.manga.MangaResponse
import com.gnoemes.shimori.source.shikimori.models.rates.ShikimoriRateStatus
import com.gnoemes.shimori.source.shikimori.models.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shimori.source.shikimori.models.rates.UserRateResponse
import com.gnoemes.shimori.source.shikimori.models.user.FavoriteListResponse
import com.gnoemes.shimori.source.shikimori.models.user.UserBriefResponse
import com.gnoemes.shimori.source.shikimori.models.user.UserDetailsResponse
import com.gnoemes.shimori.source.shikimori.services.AnimeService
import com.gnoemes.shimori.source.shikimori.services.CharacterService
import com.gnoemes.shimori.source.shikimori.services.GraphQlService
import com.gnoemes.shimori.source.shikimori.services.MangaService
import com.gnoemes.shimori.source.shikimori.services.PeopleService
import com.gnoemes.shimori.source.shikimori.services.RanobeService
import com.gnoemes.shimori.source.shikimori.services.RateService
import com.gnoemes.shimori.source.shikimori.services.StudioService
import com.gnoemes.shimori.source.shikimori.services.UserService
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriApi(
    private val values: ShikimoriValues,
    private val client: ShikimoriKtor,
    private val graphql: ShikimoriApollo,
    private val logger: Logger,
) {

    private val apiUrl = "${values.url}/$API_PATH"

    private companion object {
        const val API_PATH = "api"
    }

    private suspend fun <D : Query.Data> query(query: Query<D>) = graphql.query(query).execute()

    ///////////////////////////////////////////////////////
    // Services
    ///////////////////////////////////////////////////////

    internal val apollo: GraphQlService by lazy { GraphQlServiceImpl() }
    internal val rate: RateService by lazy { RateServiceImpl() }
    internal val user: UserService by lazy { UserServiceImpl() }
    internal val anime: AnimeService by lazy { AnimeServiceImpl() }
    internal val manga: MangaService by lazy { MangaServiceImpl() }
    internal val ranobe: RanobeService by lazy { RanobeServiceImpl() }
    internal val character: CharacterService by lazy { CharacterServiceImpl() }
    internal val people: PeopleService by lazy { PeopleServiceImpl() }
    internal val studio: StudioService by lazy { StudioServiceImpl() }

    ///////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////

    private inner class RateServiceImpl : RateService {
        private val serviceUrl = "$apiUrl/v2/user_rates"

        override suspend fun userRates(
            userId: Long,
            targetId: Long?,
            targetType: ShikimoriTargetType?,
            status: ShikimoriRateStatus?,
            page: Int?,
            limit: Int?
        ): List<UserRateResponse> {
            return client.get {
                url(serviceUrl)
                parameter("user_id", userId)
                targetId?.let { parameter("target_id", it) }
                targetType?.let { parameter("target_type", it) }
                status?.let { parameter("status", it.status) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
            }.body()
        }

        override suspend fun get(id: Long): UserRateResponse {
            return client.get {
                url("$serviceUrl/$id")
            }.body()
        }

        override suspend fun delete(id: Long) {
            return client.delete {
                url("$serviceUrl/$id")
            }.body()
        }

        override suspend fun create(request: UserRateCreateOrUpdateRequest): UserRateResponse {
            return client.post {
                url(serviceUrl)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }

        override suspend fun update(
            id: Long,
            request: UserRateCreateOrUpdateRequest
        ): UserRateResponse {
            return client.patch {
                url("$serviceUrl/$id")
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()
        }

        override suspend fun increment(id: Long) {
            return client.post {
                url("$serviceUrl/$id/increment")
            }.body()
        }
    }

    private inner class UserServiceImpl : UserService {
        private val serviceUrl = "$apiUrl/users"

        override suspend fun getMeShort(): UserBriefResponse {
            return client.get {
                url("$serviceUrl/whoami")
            }.body()
        }

        override suspend fun getShort(id: Long): UserBriefResponse {
            return client.get {
                url("$serviceUrl/$id/info")
            }.body()
        }

        override suspend fun getFull(id: Long): UserDetailsResponse {
            return client.get {
                url("$serviceUrl/$id")
            }.body()
        }

        override suspend fun getFriends(id: Long): List<UserBriefResponse> {
            return client.get {
                url("$serviceUrl/$id/friends")
            }.body()
        }

        override suspend fun getList(page: Int, limit: Int): List<UserBriefResponse> {
            return client.get {
                url(serviceUrl)
                parameter("page", page)
                parameter("limit", limit)
            }.body()
        }

        override suspend fun getFavorites(id: Long): FavoriteListResponse {
            return client.get {
                url("$serviceUrl/$id/favourites")
            }.body()
        }

        override suspend fun getClubs(id: Long): List<com.gnoemes.shimori.source.shikimori.models.club.ClubResponse> {
            return client.get {
                url("$serviceUrl/$id/clubs")
            }.body()
        }

        override suspend fun addToFriends(id: Long) {
            return client.post {
                url("$serviceUrl/friends/$id")
            }.body()
        }

        override suspend fun deleteFriend(id: Long) {
            return client.delete {
                url("$serviceUrl/friends/$id")
            }.body()
        }

        override suspend fun ignore(id: Long) {
            return client.post {
                url("$serviceUrl/v2/users/$id/ignore")
            }.body()
        }

        override suspend fun unignore(id: Long) {
            return client.delete {
                url("$serviceUrl/v2/users/$id/ignore")
            }.body()
        }
    }

    private inner class AnimeServiceImpl : AnimeService {
        private val serviceUrl = "$apiUrl/animes"

        override suspend fun getSimilar(id: Long): List<com.gnoemes.shimori.source.shikimori.models.anime.AnimeResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }

        override suspend fun calendar(): List<CalendarResponse> {
            return client.get {
                url("$apiUrl/calendar")
            }.body()
        }
    }

    private inner class MangaServiceImpl :
        MangaService {
        private val serviceUrl = "$apiUrl/mangas"

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }
    }

    private inner class RanobeServiceImpl : RanobeService {
        private val serviceUrl = "$apiUrl/mangas"

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }
    }

    private inner class CharacterServiceImpl : CharacterService {
    }

    private inner class PeopleServiceImpl : PeopleService {
    }


    private inner class StudioServiceImpl : StudioService {
        override suspend fun getAll(): List<StudioResponse> {
            return client.get {
                url("$apiUrl/studios")
            }.body()
        }
    }

    private inner class GraphQlServiceImpl : GraphQlService {
        override suspend fun <D : Query.Data> query(query: Query<D>) =
            this@ShikimoriApi.query(query)
    }

}