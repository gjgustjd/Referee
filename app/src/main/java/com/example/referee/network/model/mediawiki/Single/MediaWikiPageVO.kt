package com.example.referee.network.model.mediawiki.Single

import com.google.gson.annotations.SerializedName

data class MediaWikiPageVO(
    @SerializedName("query") val query: MediaWikiQuery? = null,
)