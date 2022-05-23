package com.example.eshop.di

import com.example.eshop.data.remote.Constants.BASE_URL
import com.example.eshop.data.remote.Constants.CONSUMER_KEY
import com.example.eshop.data.remote.Constants.CONSUMER_SECRET
import com.example.eshop.data.remote.network.WooCommerceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @IoDispatcher
    fun provideDispatchers(): CoroutineDispatcher = Dispatchers.IO

    @Provides
    @Singleton
    fun provideClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(provideLoggingInterceptor())
            .addInterceptor(provideInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request()
            val url = request.url().newBuilder()
                .addQueryParameter("consumer_key", CONSUMER_KEY)
                .addQueryParameter("consumer_secret", CONSUMER_SECRET)
                .build()
            val new = request.newBuilder()
                .url(url)
                .build()
            it.proceed(new)
        }
    }

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Singleton
    @Provides
    fun jsonConvertorFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): WooCommerceApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(jsonConvertorFactory())
            .build()
            .create(WooCommerceApi::class.java)
    }
}