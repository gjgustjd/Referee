package com.example.referee.network.model.mediawiki.List

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("title") val title: String,
    @SerializedName("pageid") val pageid: Int,
    @SerializedName("snippet") var snippet: String,
)