package com.gnoemes.shikimori

import com.gnoemes.shikimori.repositories.ShikimoriAnimeDataSource
import com.gnoemes.shikimori.repositories.ShikimoriUserDataSource
import com.gnoemes.shikimori.services.AnimeService
import com.gnoemes.shikimori.services.AuthService
import com.gnoemes.shikimori.services.RateService
import com.gnoemes.shikimori.services.UserService
import com.gnoemes.shikimori.util.*
import com.gnoemes.shimori.base.di.Auth
import com.gnoemes.shimori.base.di.Shikimori
import com.gnoemes.shimori.data_base.sources.AnimeDataSource
import com.gnoemes.shimori.data_base.sources.UserDataSource
import com.gnoemes.shimori.model.ShimoriConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.joda.time.DateTime
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [
    RetrofitModule::class,
    AuthNetworkModule::class,
    CommonNetworkModule::class,
    ApiServices::class,
    RepositoryModule::class
])
class ShikimoriModule

@Module
internal abstract class RepositoryModule {

    @Binds
    @Singleton
    @Shikimori
    abstract fun bindAnimeSource(source: ShikimoriAnimeDataSource): AnimeDataSource

    @Binds
    @Singleton
    @Shikimori
    abstract fun bindUserSource(source: ShikimoriUserDataSource): UserDataSource
}


@Module
internal class ApiServices {

    @Provides
    @Singleton
    fun animeService(retrofit: Retrofit): AnimeService {
        return retrofit.create(AnimeService::class.java)
    }

    @Provides
    @Singleton
    fun userService(@Auth retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun rateService(@Auth retrofit: Retrofit): RateService {
        return retrofit.create(RateService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }
}

@Module
internal class RetrofitModule {

    @Provides
    @Singleton
    fun provideUserInterceptor(): UserAgentInterceptor =
        UserAgentInterceptor()

    @Provides
    @Singleton
    @Named("okhttp-default")
    fun provideDefaultOkHttp(
        interceptor: HttpLoggingInterceptor,
        userAgentInterceptor: UserAgentInterceptor,
        @Named("cache") cacheDir: File
    ) = OkHttpClient.Builder()
        .apply {
            addNetworkInterceptor(userAgentInterceptor)
            addNetworkInterceptor(interceptor)
            connectTimeout(ShimoriConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            readTimeout(ShimoriConstants.DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            cache(Cache(File(cacheDir, "okhttp_cache"), 10 * 1024 * 1024))
        }

    @Provides
    @Singleton
    fun provideFactory(gson: Gson): Converter.Factory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideGson(dateTimeResponseConverter: DateTimeResponseConverter) = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, dateTimeResponseConverter)
        .create()

    @Provides
    @Singleton
    fun provideDateResponseConverter(): DateTimeResponseConverter = DateTimeResponseConverterImpl()

}

@Module
internal class CommonNetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(@Named("okhttp-default") builder: OkHttpClient.Builder) =
        builder.build()

    @Provides
    @Singleton
    fun provideRetrofitBuilder(factory: Converter.Factory, client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(factory)
    }

    @Provides
    @Singleton
    fun provideRetrofit(builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(ShimoriConstants.ShikimoriBaseUrl).build()
    }
}

@Module
internal class AuthNetworkModule {
    //TODO

    @Provides
    @Singleton
    @Auth
    fun provideAuthOkHttpClient(
        @Named("okhttp-default") builder: OkHttpClient.Builder,
        @Auth tokenInterceptor: ShikimoriTokenInterceptor,
        @Auth authenticator: Authenticator
    ) =
        builder.apply {
            addInterceptor(tokenInterceptor)
            authenticator(authenticator)
        }.build()

    @Provides
    @Singleton
    @Auth
    fun provideRetrofitBuilder(factory: Converter.Factory, @Auth client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(factory)
    }

    @Provides
    @Singleton
    @Auth
    fun provideRetrofit(@Auth builder: Retrofit.Builder): Retrofit {
        return builder.baseUrl(ShimoriConstants.ShikimoriBaseUrl).build()
    }

    @Provides
    @Singleton
    @Auth
    fun provideTokenInterceptor(source: com.gnoemes.shikimori.Shikimori) =
        ShikimoriTokenInterceptor(source)

    @Provides
    @Singleton
    @Auth
    fun provideAuth(shikimori: com.gnoemes.shikimori.Shikimori): Authenticator =
        ShikimoriAuthenticator(shikimori)
}