package com.example.referee.network.model.mediawiki.List

import com.example.referee.network.model.mediawiki.SIngle.MediaWikiWarnings
import com.google.gson.annotations.SerializedName

data class MediaWikiSearchListVO(
    @SerializedName("batchcomplete") val batchcomplete: String,
    @SerializedName("continue") val continueInfo:ContinueInfo,
    @SerializedName("query") val query: MediaWIkiListQuery,
    @SerializedName("warnings") val warnings: MediaWikiWarnings? = null
)