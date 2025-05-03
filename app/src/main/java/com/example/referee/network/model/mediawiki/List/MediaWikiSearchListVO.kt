package com.example.referee.network.model.mediawiki.List

import com.google.gson.annotations.SerializedName

data class MediaWikiSearchListVO(
    @SerializedName("continue") val continueInfo:ContinueInfo,
    @SerializedName("query") val query: MediaWikiListQuery,
)