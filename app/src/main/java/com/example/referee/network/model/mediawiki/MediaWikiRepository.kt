package com.example.referee.network.model.mediawiki

import com.example.referee.network.MediaWikiApiUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object MediaWikiRepository {

    fun getSinglePageByTitle(title:String) =
        MediaWikiApiUtil.getMediaWikiAPI()
            .searchAndGetIngredientPageByTitle(title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun getSinglePageById(pageId:Int) =
        MediaWikiApiUtil.getMediaWikiAPI()
            .searchPageByPageId(pageId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun searchAndGetResults(keyword:String) =
        MediaWikiApiUtil.getMediaWikiAPI()
            .searchIngredientResults(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}