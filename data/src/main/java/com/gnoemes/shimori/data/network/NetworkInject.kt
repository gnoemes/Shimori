package com.gnoemes.shimori.data.network

import com.gnoemes.shimori.base.di.Auth
import com.gnoemes.shimori.data.util.DateTimeResponseConverter
import com.gnoemes.shimori.data.util.DateTimeResponseConverterImpl
import com.gnoemes.shimori.data.util.UserAgentInterceptor
import com.gnoemes.shimori.model.ShimoriConstants
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
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

@Module(includes = [ApiModule::class])
class NetworkModule

@Module(includes = [RetrofitModule::class, AuthNetworkModule::class, CommonNetworkModule::class])
class ApiModule {

    @Singleton
    @Provides
    fun bindAnimesApi(@Auth retrofit: Retrofit): AnimeApi {
        return retrofit.create(AnimeApi::class.java)
    }
}

@Module
class RetrofitModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideFactory(gson: Gson) = GsonConverterFactory.create(gson)


    @Provides
    @Singleton
    fun provideGson(dateTimeResponseConverter: DateTimeResponseConverter) = GsonBuilder()
        .registerTypeAdapter(DateTime::class.java, dateTimeResponseConverter)
        .create()

    @Provides
    @Singleton
    fun provideDateResponseConverter(): DateTimeResponseConverter = DateTimeResponseConverterImpl()

    @Provides
    @Singleton
    fun provideUserInterceptor(): UserAgentInterceptor = UserAgentInterceptor()

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

}

@Module
class CommonNetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(builder: OkHttpClient.Builder) = builder.build()

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
class AuthNetworkModule {
    //TODO

    @Provides
    @Singleton
    @Auth
    fun provideAuthOkHttpClient(builder: OkHttpClient.Builder) = builder.apply {
        //TODO auth
    }.build()
}

