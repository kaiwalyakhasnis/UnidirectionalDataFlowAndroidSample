package com.exercise.itunessearch.common

import android.content.Context
import com.bumptech.glide.module.AppGlideModule
import dagger.Component
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Singleton
@Component(modules = [(AppModule::class)])
interface AppComponent {
    fun getRetrofit(): Retrofit
    fun getOkHttp3IdlingResource(): OkHttp3IdlingResource
}

@Module
class AppModule(private val baseUrl: String) {

    @Singleton
    @Provides
    fun providesOkHttp3IdlingResource(okHttpClient: OkHttpClient) =
            OkHttp3IdlingResource.create("OkHttp", okHttpClient)

    @Singleton
    @Provides
    fun providesOkhttp(): OkHttpClient = OkHttpClient()
            .newBuilder()
            .build()

    @Singleton
    @Provides
    fun providesRetrofit(okHttpClient:OkHttpClient): Retrofit = Retrofit
            .Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
}