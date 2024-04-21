package com.example.referee.network.model.mediawiki.Single

import com.google.gson.annotations.SerializedName

data class MediaWikiWarningsExtracts(
    @SerializedName("*") val content: String
)