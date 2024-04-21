package com.example.referee.network.model.mediawiki.Single

import com.google.gson.annotations.SerializedName

data class MediaWikiPageVO(
    @SerializedName("batchcomplete") val batchcomplete: String? = null,
    @SerializedName("query") val query: MediaWikiQuery? = null,
    @SerializedName("warnings") val warnings: MediaWikiWarnings? = null
)