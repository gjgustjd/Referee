package com.example.referee.network.model.mediawiki.SIngle

import com.google.gson.annotations.SerializedName

data class MediaWikiPageVO(
    @SerializedName("batchcomplete") val batchcomplete: String,
    @SerializedName("query") val query: MediaWikiQuery,
    @SerializedName("warnings") val warnings: MediaWikiWarnings? = null
)