package com.example.referee.network.model.mediawiki

import com.example.referee.network.MediaWikiApiUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


object MediaWikiRepository {

    fun getSinglePage(title:String) =
        MediaWikiApiUtil.getMediaWikiAPI()
            .searchAndGetIngredientPage(title)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

    fun searchAndGetResults(keyword:String) =
        MediaWikiApiUtil.getMediaWikiAPI()
            .searchIngredientResults(keyword)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}