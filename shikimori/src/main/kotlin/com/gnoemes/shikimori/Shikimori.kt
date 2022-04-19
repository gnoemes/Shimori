package com.gnoemes.shikimori

import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shikimori.entities.anime.ScreenshotResponse
import com.gnoemes.shikimori.entities.auth.TokenResponse
import com.gnoemes.shikimori.entities.club.ClubResponse
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shikimori.entities.user.FavoriteListResponse
import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shikimori.entities.user.UserDetailsResponse
import com.gnoemes.shikimori.entities.user.UserHistoryResponse
import com.gnoemes.shikimori.services.*
import com.gnoemes.shimori.base.core.entities.Platform
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.json.Json

internal typealias LoggingType = io.ktor.client.plugins.logging.Logger

@com.gnoemes.shimori.data.base.utils.Shikimori
class Shikimori constructor(
    engine: HttpClientEngine,
    private val platform: Platform,
    private val storage: ShimoriStorage,
    private val logger: Logger,
) {
    private var _authErrorState = MutableStateFlow<Boolean?>(null)
    val authErrorState: StateFlow<Boolean?> = _authErrorState

    private val client = createClient(engine)
    private val API_URL = "${platform.shikimoriURL}$API_PATH"

    companion object {
        const val API_PATH = "/api"
    }

    private fun createClient(engine: HttpClientEngine) = HttpClient(engine) {
        install(Logging) {
            logger =
                if (platform.type == Platform.Type.Android) LoggingType.ANDROID else LoggingType.DEFAULT
            level = if (platform.debug) LogLevel.BODY else LogLevel.NONE
        }

        install(ContentNegotiation) {
            json(json = Json {
                prettyPrint = true
                isLenient = true
            })
        }

        install(Auth) {
            bearer {
                val accessToken = storage.shikimoriAccessToken
                val refreshToken = storage.shikimoriRefreshToken
                if (accessToken != null && refreshToken != null) {
                    loadTokens { BearerTokens(accessToken, refreshToken) }
                }

                refreshTokens {
                    val rfToken =
                        storage.shikimoriRefreshToken ?: return@refreshTokens null
                    val tokens = auth.refreshToken(rfToken)
                    if (tokens != null) {
                        storage.shikimoriAccessToken = tokens.accessToken
                        storage.shikimoriRefreshToken = tokens.refreshToken
                    }
                    tokens?.let { BearerTokens(it.accessToken, it.refreshToken) }
                }
            }
        }
    }

    private suspend fun onAuthExpired() {
        _authErrorState.emit(true)
    }

    ///////////////////////////////////////////////////////
    // Services
    ///////////////////////////////////////////////////////

    private val auth: AuthService by lazy { AuthServiceImpl() }
    internal val rate: RateService by lazy { RateServiceImpl() }
    internal val user: UserService by lazy { UserServiceImpl() }
    internal val anime: AnimeService by lazy { AnimeServiceImpl() }
    internal val manga: MangaService by lazy { MangaServiceImpl() }
    internal val ranobe: RanobeService by lazy { RanobeServiceImpl() }

    ///////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////

    private inner class AuthServiceImpl : AuthService {
        override suspend fun refreshToken(refreshToken: String): TokenResponse? {
            return try {
                client.post {
                    url("$API_URL/oauth/token")
                    parameter("client_id", platform.shikimoriClientId)
                    parameter("client_secret", platform.shikimoriSecretKey)
                    parameter("redirect_uri", platform.shikimoriRedirect)
                    parameter("refresh_token", refreshToken)
                    parameter("grant_type", "refresh_token")
                }.body<TokenResponse>()
            } catch (e: Exception) {
                logger.e("Failed refresh token update", t = e, tag = "Shikimori")
                onAuthExpired()
                null
            }
        }
    }

    private inner class RateServiceImpl : RateService {
        private val serviceUrl = "$API_URL/v2/user_rates"

        override suspend fun userRates(
            userId: Long,
            targetId: Long?,
            targetType: RateTargetType?,
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

                clientHeader()
            }.body()
        }

        override suspend fun get(id: Long): UserRateResponse {
            return client.get {
                url("$serviceUrl/$id")
                clientHeader()
            }.body()
        }

        override suspend fun delete(id: Long) {
            return client.delete {
                url("$serviceUrl/$id")
                clientHeader()
            }.body()
        }

        override suspend fun create(request: UserRateCreateOrUpdateRequest): UserRateResponse {
            return client.post {
                url(serviceUrl)
                setBody(request)
                clientHeader()
            }.body()
        }

        override suspend fun update(
            id: Long,
            request: UserRateCreateOrUpdateRequest
        ): UserRateResponse {
            return client.patch {
                url("$serviceUrl/$id")
                setBody(request)
                clientHeader()
            }.body()
        }

        override suspend fun increment(id: Long) {
            return client.post {
                url("$serviceUrl/$id/increment")
                clientHeader()
            }.body()
        }
    }

    private inner class UserServiceImpl : UserService {
        private val serviceUrl = "$API_URL/users"

        override suspend fun getMeShort(): UserBriefResponse {
            return client.get {
                url("$serviceUrl/whoami")
                clientHeader()
            }.body()
        }

        override suspend fun getShort(id: Long): UserBriefResponse {
            return client.get {
                url("$serviceUrl/$id/info")
                clientHeader()
            }.body()
        }

        override suspend fun getFull(id: Long): UserDetailsResponse {
            return client.get {
                url("$serviceUrl/$id")
                clientHeader()
            }.body()
        }

        override suspend fun getFriends(id: Long): List<UserBriefResponse> {
            return client.get {
                url("$serviceUrl/$id/friends")
                clientHeader()
            }.body()
        }

        override suspend fun getList(page: Int, limit: Int): List<UserBriefResponse> {
            return client.get {
                url(serviceUrl)
                parameter("page", page)
                parameter("limit", limit)
                clientHeader()
            }.body()
        }

        override suspend fun getFavorites(id: Long): FavoriteListResponse {
            return client.get {
                url("$serviceUrl/$id/favourites")
                clientHeader()
            }.body()
        }

        override suspend fun getUserHistory(
            id: Long,
            page: Int,
            limit: Int
        ): List<UserHistoryResponse> {
            return client.get {
                url("$serviceUrl/$id/history")
                clientHeader()
            }.body()
        }

        override suspend fun getClubs(id: Long): List<ClubResponse> {
            return client.get {
                url("$serviceUrl/$id/clubs")
                clientHeader()
            }.body()
        }

        override suspend fun addToFriends(id: Long) {
            return client.post {
                url("$API_URL/friends/$id")
                clientHeader()
            }.body()
        }

        override suspend fun deleteFriend(id: Long) {
            return client.delete {
                url("$serviceUrl/friends/$id")
                clientHeader()
            }.body()
        }

        override suspend fun ignore(id: Long) {
            return client.post {
                url("$serviceUrl/v2/users/$id/ignore")
                clientHeader()
            }.body()
        }

        override suspend fun unignore(id: Long) {
            return client.delete {
                url("$serviceUrl/v2/users/$id/ignore")
                clientHeader()
            }.body()
        }
    }

    private inner class AnimeServiceImpl : AnimeService {
        private val serviceUrl = "$API_URL/animes"

        override suspend fun search(filters: Map<String, String>): List<AnimeResponse> {
            return client.get {
                url(serviceUrl)
                with(url.parameters) {
                    filters.forEach { (key, value) -> append(key, value) }
                }
                clientHeader()
            }.body()
        }

        override suspend fun getDetails(id: Long): AnimeDetailsResponse {
            return client.get {
                url("$serviceUrl/$id")
                clientHeader()
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
                clientHeader()
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<AnimeResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")
                clientHeader()
            }.body()
        }

        override suspend fun getRelated(id: Long): List<AnimeResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
                clientHeader()
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
                clientHeader()
            }.body()
        }

        override suspend fun getScreenshots(id: Long): List<ScreenshotResponse> {
            return client.get {
                url("$serviceUrl/$id/screenshots")
                clientHeader()
            }.body()
        }

        override suspend fun calendar(): List<CalendarResponse> {
            return client.get {
                url("$API_URL/calendar")
                clientHeader()
            }.body()
        }

        override suspend fun getUserRates(
            id: Long,
            status: ShikimoriRateStatus?,
            page: Int,
            limit: Int,
            censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/anime_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
                clientHeader()
            }.body()
        }
    }

    private inner class MangaServiceImpl : MangaService {
        private val serviceUrl = "$API_URL/mangas"

        override suspend fun search(filters: Map<String, String>): List<MangaResponse> {
            return client.get {
                url(serviceUrl)
                with(url.parameters) {
                    filters.forEach { (key, value) -> append(key, value) }
                }
                clientHeader()
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
                clientHeader()
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")
                clientHeader()
            }.body()
        }

        override suspend fun getRelated(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
                clientHeader()
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
                clientHeader()
            }.body()
        }

        override suspend fun getUserRates(
            id: Long,
            status: ShikimoriRateStatus?,
            page: Int,
            limit: Int,
            censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/manga_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
                clientHeader()
            }.body()
        }
    }

    private inner class RanobeServiceImpl : RanobeService {
        private val serviceUrl = "$API_URL/ranobe"

        override suspend fun search(filters: Map<String, String>): List<MangaResponse> {
            return client.get {
                url(serviceUrl)
                with(url.parameters) {
                    filters.forEach { (key, value) -> append(key, value) }
                }
                clientHeader()
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
                clientHeader()
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")
                clientHeader()
            }.body()
        }

        override suspend fun getRelated(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
                clientHeader()
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
                clientHeader()
            }.body()
        }

        override suspend fun getUserRates(
            id: Long,
            status: ShikimoriRateStatus?,
            page: Int,
            limit: Int,
            censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/manga_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
                clientHeader()
            }.body()
        }
    }


    private fun HttpRequestBuilder.clientHeader() =
        header("User-Agent", platform.shikimoriUserAgent)

}