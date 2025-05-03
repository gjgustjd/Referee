package com.example.referee.network

import com.example.referee.network.model.mediawiki.MediaWikiApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object MediaWikiApiUtil {

    private fun getMediaWikiRetrofitBuilder(): Retrofit {
        val gson : Gson = GsonBuilder()
            .setLenient()
            .create()
        val client = OkHttpClient.Builder()
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        return Retrofit.Builder()
            .baseUrl(LinkUtils.MEDIAWIKI_API_REQUEST_HOST)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    fun getMediaWikiAPI(): MediaWikiApi =
        getMediaWikiRetrofitBuilder().create(MediaWikiApi::class.java)
}