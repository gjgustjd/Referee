package com.example.referee.network.model.mediawiki

import com.example.referee.network.LinkUtils
import com.example.referee.network.model.mediawiki.List.MediaWikiSearchListVO
import com.example.referee.network.model.mediawiki.SIngle.MediaWikiPageVO
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MediaWikiApi {
    companion object {
        const val PATH = "${LinkUtils.MEDIAWIKI_URL}api.php?action=query"
        const val PROP = "prop=pageimages|extracts"
        const val REDIRECTS = "redirects"
        const val FORMAT_JSON = "format=json"
        const val LIST_SEARCH = "list=search"
    }

    @GET("$PATH&$FORMAT_JSON&$PROP&$REDIRECTS")
    fun searchAndGetIngredientPageByTitle(@Query("titles") title: String): Single<MediaWikiPageVO>

    @GET("$PATH&$FORMAT_JSON&$PROP")
    fun searchPageByPageId(@Query("pageIds") pageId: String): Single<MediaWikiPageVO>

    @GET("$PATH&$LIST_SEARCH&$FORMAT_JSON")
    fun searchIngredientResults(@Query("srsearch") keyword: String): Single<MediaWikiSearchListVO>
}