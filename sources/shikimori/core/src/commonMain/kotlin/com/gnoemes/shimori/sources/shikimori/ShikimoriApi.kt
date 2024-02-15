package com.gnoemes.shimori.sources.shikimori

import com.apollographql.apollo3.api.Query
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.sources.shikimori.models.anime.AnimeResponse
import com.gnoemes.shimori.sources.shikimori.models.anime.CalendarResponse
import com.gnoemes.shimori.sources.shikimori.models.auth.TokenResponse
import com.gnoemes.shimori.sources.shikimori.models.club.ClubResponse
import com.gnoemes.shimori.sources.shikimori.models.manga.MangaResponse
import com.gnoemes.shimori.sources.shikimori.models.rates.ShikimoriRateStatus
import com.gnoemes.shimori.sources.shikimori.models.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shimori.sources.shikimori.models.rates.UserRateResponse
import com.gnoemes.shimori.sources.shikimori.models.user.FavoriteListResponse
import com.gnoemes.shimori.sources.shikimori.models.user.UserBriefResponse
import com.gnoemes.shimori.sources.shikimori.models.user.UserDetailsResponse
import com.gnoemes.shimori.sources.shikimori.models.user.UserHistoryResponse
import com.gnoemes.shimori.sources.shikimori.services.AnimeService
import com.gnoemes.shimori.sources.shikimori.services.AuthService
import com.gnoemes.shimori.sources.shikimori.services.CharacterService
import com.gnoemes.shimori.sources.shikimori.services.MangaService
import com.gnoemes.shimori.sources.shikimori.services.RanobeService
import com.gnoemes.shimori.sources.shikimori.services.RateService
import com.gnoemes.shimori.sources.shikimori.services.UserService
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
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
        const val API_PATH = "/api"
    }

    private suspend fun <D : Query.Data> query(query: Query<D>) =
        graphql.query(query).execute()

    ///////////////////////////////////////////////////////
    // Services
    ///////////////////////////////////////////////////////

    internal val auth: AuthService by lazy { AuthServiceImpl() }
    internal val rate: RateService by lazy { RateServiceImpl() }
    internal val user: UserService by lazy { UserServiceImpl() }
    internal val anime: AnimeService by lazy { AnimeServiceImpl() }
    internal val manga: MangaService by lazy { MangaServiceImpl() }
    internal val ranobe: RanobeService by lazy { RanobeServiceImpl() }
    internal val character: CharacterService by lazy { CharacterServiceImpl() }


    ///////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////

    private inner class AuthServiceImpl : AuthService {
        private val tokenEndpoint = "${values.url}/oauth/token"
        override suspend fun accessToken(authCode: String): TokenResponse? {
            return try {
                client.post {
                    url(tokenEndpoint)
                    parameter("grant_type", "authorization_code")
                    parameter("client_id", values.clientId)
                    parameter("client_secret", values.secretKey)
                    parameter("redirect_uri", values.oauthRedirect)
                    parameter("code", authCode)
                }.body()
            } catch (e: Exception) {
                logger.e(e, tag = "Shikimori") { "Failed get access token" }
//                onAuthError(e.localizedMessage)
                null
            }
        }

        override suspend fun refreshToken(
            refreshToken: String,
            block: HttpRequestBuilder.() -> Unit
        ): TokenResponse? {
            return try {
                client.post {
                    url(tokenEndpoint)
                    parameter("client_id", values.clientId)
                    parameter("client_secret", values.secretKey)
                    parameter("refresh_token", refreshToken)
                    parameter("grant_type", "refresh_token")
                    block()
                }.body()
            } catch (e: Exception) {
                logger.e(e, tag = "Shikimori") { "Failed refresh token update" }
//                onAuthExpired()
                null
            }
        }
    }

    private inner class RateServiceImpl : RateService {
        private val serviceUrl = "$apiUrl/v2/user_rates"

        override suspend fun <D : Query.Data> graphql(query: Query<D>) = query(query)

        override suspend fun userRates(
            userId: Long,
            targetId: Long?,
            targetType: TrackTargetType?,
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

        override suspend fun getUserHistory(
            id: Long, page: Int, limit: Int
        ): List<UserHistoryResponse> {
            return client.get {
                url("$serviceUrl/$id/history")
            }.body()
        }

        override suspend fun getClubs(id: Long): List<ClubResponse> {
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

        override suspend fun <D : Query.Data> graphql(query: Query<D>) = query(query)

        override suspend fun getSimilar(id: Long): List<AnimeResponse> {
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

    private inner class MangaServiceImpl : MangaService {
        private val serviceUrl = "$apiUrl/mangas"

        override suspend fun <D : Query.Data> graphql(query: Query<D>) = query(query)

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }
    }

    private inner class RanobeServiceImpl : RanobeService {
        private val serviceUrl = "$apiUrl/mangas"

        override suspend fun <D : Query.Data> graphql(query: Query<D>) = query(query)

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }
    }

    private inner class CharacterServiceImpl : CharacterService {
        override suspend fun <D : Query.Data> graphql(query: Query<D>) = query(query)
    }

}