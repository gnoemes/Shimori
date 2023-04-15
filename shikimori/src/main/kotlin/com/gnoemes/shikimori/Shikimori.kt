package com.gnoemes.shikimori

import com.gnoemes.shikimori.entities.anime.AnimeDetailsResponse
import com.gnoemes.shikimori.entities.anime.AnimeResponse
import com.gnoemes.shikimori.entities.anime.AnimeScreenshotResponse
import com.gnoemes.shikimori.entities.anime.CalendarResponse
import com.gnoemes.shikimori.entities.auth.TokenResponse
import com.gnoemes.shikimori.entities.club.ClubResponse
import com.gnoemes.shikimori.entities.common.LinkResponse
import com.gnoemes.shikimori.entities.common.RolesResponse
import com.gnoemes.shikimori.entities.manga.MangaDetailsResponse
import com.gnoemes.shikimori.entities.manga.MangaResponse
import com.gnoemes.shikimori.entities.rates.RateResponse
import com.gnoemes.shikimori.entities.rates.ShikimoriRateStatus
import com.gnoemes.shikimori.entities.rates.UserRateCreateOrUpdateRequest
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shikimori.entities.roles.CharacterDetailsResponse
import com.gnoemes.shikimori.entities.roles.CharacterResponse
import com.gnoemes.shikimori.entities.user.FavoriteListResponse
import com.gnoemes.shikimori.entities.user.UserBriefResponse
import com.gnoemes.shikimori.entities.user.UserDetailsResponse
import com.gnoemes.shikimori.entities.user.UserHistoryResponse
import com.gnoemes.shikimori.services.AnimeService
import com.gnoemes.shikimori.services.AuthService
import com.gnoemes.shikimori.services.CharacterService
import com.gnoemes.shikimori.services.MangaService
import com.gnoemes.shikimori.services.RanobeService
import com.gnoemes.shikimori.services.RateService
import com.gnoemes.shikimori.services.UserService
import com.gnoemes.shimori.base.core.entities.SourcePlatformValues
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.entities.auth.ShikimoriAuthState
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.plugin
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

class Shikimori(
    private val client: HttpClient,
    private val platform : SourcePlatformValues,
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

    private val API_URL = "${platform.url}$API_PATH"

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
                    BearerTokens(accessToken, refreshToken)
                } else null
            }

            refreshTokens {
                val oldTokens = this.oldTokens
                if (oldTokens?.refreshToken == null) {
                    onAuthExpired()
                    return@refreshTokens null
                }


                val tokenResponse = auth.refreshToken(oldTokens.refreshToken) {
                    markAsRefreshTokenRequest()
                }

                if (tokenResponse == null || tokenResponse.isEmpty) {
                    onAuthExpired()
                    return@refreshTokens null
                }

                storage.shikimoriAccessToken = tokenResponse.accessToken
                storage.shikimoriRefreshToken = tokenResponse.refreshToken

                _state.emit(ShikimoriAuthState.LOGGED_IN)
                BearerTokens(tokenResponse.accessToken!!, tokenResponse.refreshToken!!)
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
    internal val character: CharacterService by lazy { CharacterServiceImpl() }

    ///////////////////////////////////////////////////////
    // Implementations
    ///////////////////////////////////////////////////////

    private inner class AuthServiceImpl : AuthService {
        private val tokenEndpoint = "${platform.url}/oauth/token"
        override suspend fun accessToken(authCode: String): TokenResponse? {
            return try {
                client.post {
                    url(tokenEndpoint)
                    parameter("grant_type", "authorization_code")
                    parameter("client_id", platform.clientId)
                    parameter("client_secret", platform.secretKey)
                    parameter("redirect_uri", platform.oauthRedirect)
                    parameter("code", authCode)
                }.body()
            } catch (e: Exception) {
                logger.e("Failed get access token", t = e, tag = "Shikimori")
                onAuthError(e.localizedMessage)
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
                    parameter("client_id", platform.clientId)
                    parameter("client_secret", platform.secretKey)
                    parameter("refresh_token", refreshToken)
                    parameter("grant_type", "refresh_token")
                    block()
                }.body()
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

        override suspend fun getScreenshots(id: Long): List<AnimeScreenshotResponse> {
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

        override suspend fun getDetails(id: Long): MangaDetailsResponse {
            return client.get {
                url("$serviceUrl/$id")
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

    private inner class CharacterServiceImpl : CharacterService {
        private val serviceUrl = "$API_URL/characters"

        override suspend fun search(filters: Map<String, String>): List<CharacterResponse> {
            return client.get {
                url("$serviceUrl/search")
                filters.entries.forEach {
                    parameter(it.key, it.value)
                }
            }.body()
        }

        override suspend fun getDetails(id: Long): CharacterDetailsResponse {
            return client.get {
                url("$serviceUrl/details/$id")
            }.body()
        }
    }
}