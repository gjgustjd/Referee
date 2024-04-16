package com.example.referee.network.model.mediawiki.List

import com.google.gson.annotations.SerializedName

data class MediaWikiListQuery(
    @SerializedName("searchInfo") val searchInfo: SearchInfo,
    @SerializedName("search") val searchResults: List<Search>
)