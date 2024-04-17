package com.example.referee.network.model.mediawiki.List

import com.google.gson.annotations.SerializedName

data class Search(
    @SerializedName("ns") val ns: Int,
    @SerializedName("title") val title: String,
    @SerializedName("pageid") val pageid: Int,
    @SerializedName("size") val size: Int,
    @SerializedName("wordcount") val wordcount: Int,
    @SerializedName("snippet") var snippet: String,
    @SerializedName("timestamp") val timestamp: String,
)