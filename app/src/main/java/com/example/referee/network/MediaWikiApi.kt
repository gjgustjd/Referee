package com.example.referee.network

import com.example.referee.network.model.mediawiki.List.MediaWikiSearchListVO
import com.example.referee.network.model.mediawiki.SIngle.MediaWikiPageVO
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiApi {
    companion object {
        const val PATH = "${LinkUtils.MEDIAWIKI_URL}/api.php?action=query"
        const val PROP = "prop=pageimages|extracts"
        const val REDIRECTS = "redirects"
        const val FORMAT_JSON = "format=json"
        const val LIST_SEARCH = "list=search"
    }

    @GET("$PATH&$FORMAT_JSON&$PROP&$REDIRECTS")
    fun searchAndGetIngredientPage(@Query("titles") title: String): MediaWikiPageVO

    @GET("$PATH&$LIST_SEARCH")
    fun searchIngredientResults(@Query("srsearch") keyword: String): MediaWikiSearchListVO
}