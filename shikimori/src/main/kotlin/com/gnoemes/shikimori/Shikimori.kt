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
import com.gnoemes.shimori.data.core.entities.auth.ShikimoriAuthState
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

@com.gnoemes.shimori.data.core.utils.Shikimori
class Shikimori(
    private val client: HttpClient,
    private val platform: Platform,
    private val storage: ShimoriStorage,
    private val logger: Logger,
) {
    private val _state = MutableStateFlow(
        if (storage.shikimoriAccessToken.isNullOrBlank()) ShikimoriAuthState.LOGGED_OUT
        else ShikimoriAuthState.LOGGED_IN
    )
    private val _errorState = MutableSharedFlow<String>()

    val authState: StateFlow<ShikimoriAuthState> get() = _state
    val authError: SharedFlow<String> get() = _errorState

    private val API_URL = "${platform.shikimori.url}$API_PATH"

    companion object {
        const val API_PATH = "/api"
    }

    init {
        configureTokens()
    }

    private fun configureTokens() {
        client.plugin(Auth).bearer {
            loadTokens {
                val accessToken = storage.shikimoriAccessToken
                val refreshToken = storage.shikimoriRefreshToken
                if (accessToken != null && refreshToken != null) {
                    onAuthSuccess(accessToken, refreshToken)
                    BearerTokens(accessToken, refreshToken)
                } else null
            }

            refreshTokens {
                val rfToken = storage.shikimoriRefreshToken ?: return@refreshTokens null
                auth.refreshToken(rfToken)?.let {
                    onAuthSuccess(it.accessToken, it.refreshToken)
                    BearerTokens(it.accessToken, it.refreshToken)
                }
            }
        }
    }

    suspend fun onAuthExpired() {
        storage.shikimoriAccessToken = null
        storage.shikimoriRefreshToken = null
        _state.emit(ShikimoriAuthState.LOGGED_OUT)
    }

    suspend fun onAuthSuccess(accessToken: String, refreshToken: String) {
        storage.shikimoriAccessToken = accessToken
        storage.shikimoriRefreshToken = refreshToken
        configureTokens()
        _state.emit(ShikimoriAuthState.LOGGED_IN)
    }

    suspend fun performTokenAuthorization(authCode: String): TokenResponse? {
        return auth.accessToken(authCode)
    }

    suspend fun onAuthError(error: String) {
        onAuthExpired()
        _errorState.emit(error)
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
        private val tokenEndpoint = "${platform.shikimori.url}/oauth/token"
        override suspend fun accessToken(authCode: String): TokenResponse? {
            return try {
                client.post {
                    url(tokenEndpoint)
                    parameter("grant_type", "authorization_code")
                    parameter("client_id", platform.shikimori.clientId)
                    parameter("client_secret", platform.shikimori.secretKey)
                    parameter("redirect_uri", platform.shikimori.oauthRedirect)
                    parameter("code", authCode)
                }.body<TokenResponse>()
            } catch (e: Exception) {
                logger.e("Failed get access token", t = e, tag = "Shikimori")
                onAuthError(e.localizedMessage)
                null
            }
        }

        override suspend fun refreshToken(refreshToken: String): TokenResponse? {
            return try {
                client.post {
                    url(tokenEndpoint)
                    parameter("client_id", platform.shikimori.clientId)
                    parameter("client_secret", platform.shikimori.secretKey)
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
        private val serviceUrl = "$API_URL/users"

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
                url("$API_URL/friends/$id")
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
        private val serviceUrl = "$API_URL/animes"

        override suspend fun search(filters: Map<String, String>): List<AnimeResponse> {
            return client.get {
                url(serviceUrl)
                with(url.parameters) {
                    filters.forEach { (key, value) -> append(key, value) }
                }
            }.body()
        }

        override suspend fun getDetails(id: Long): AnimeDetailsResponse {
            return client.get {
                url("$serviceUrl/$id")
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<AnimeResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")

            }.body()
        }

        override suspend fun getRelated(id: Long): List<AnimeResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
            }.body()
        }

        override suspend fun getScreenshots(id: Long): List<ScreenshotResponse> {
            return client.get {
                url("$serviceUrl/$id/screenshots")
            }.body()
        }

        override suspend fun calendar(): List<CalendarResponse> {
            return client.get {
                url("$API_URL/calendar")
            }.body()
        }

        override suspend fun getUserRates(
            id: Long, status: ShikimoriRateStatus?, page: Int, limit: Int, censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/anime_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
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
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")
            }.body()
        }

        override suspend fun getRelated(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
            }.body()
        }

        override suspend fun getUserRates(
            id: Long, status: ShikimoriRateStatus?, page: Int, limit: Int, censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/manga_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
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
            }.body()
        }

        override suspend fun getLinks(id: Long): List<LinkResponse> {
            return client.get {
                url("$serviceUrl/$id/external_links")
            }.body()
        }

        override suspend fun getSimilar(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/similar")
            }.body()
        }

        override suspend fun getRelated(id: Long): List<MangaResponse> {
            return client.get {
                url("$serviceUrl/$id/related")
            }.body()
        }

        override suspend fun getRoles(id: Long): List<RolesResponse> {
            return client.get {
                url("$serviceUrl/$id/roles")
            }.body()
        }

        override suspend fun getUserRates(
            id: Long, status: ShikimoriRateStatus?, page: Int, limit: Int, censored: Boolean
        ): List<RateResponse> {
            return client.get {
                url("$API_URL/users/$id/manga_rates")
                status?.let { parameter("status", it.status) }
                parameter("page", page)
                parameter("limit", limit)
                parameter("censored", censored)
            }.body()
        }
    }
}