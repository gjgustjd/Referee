package com.example.referee.network

import com.example.referee.network.model.mediawiki.MediaWikiApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MediaWikiApiUtil {

    fun getMediaWikiRetrofitBuilder() = Retrofit.Builder()
        .baseUrl(LinkUtils.MEDIAWIKI_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getMediaWikiAPI() =
        MediaWikiApiUtil.getMediaWikiRetrofitBuilder().create(MediaWikiApi::class.java)
}