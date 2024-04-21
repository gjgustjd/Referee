package com.example.referee.network.model.mediawiki.List

import com.example.referee.network.model.mediawiki.Single.MediaWikiWarnings
import com.google.gson.annotations.SerializedName

data class MediaWikiSearchListVO(
    @SerializedName("batchcomplete") val batchcomplete: String,
    @SerializedName("continue") val continueInfo:ContinueInfo,
    @SerializedName("query") val query: MediaWikiListQuery,
    @SerializedName("warnings") val warnings: MediaWikiWarnings? = null
)