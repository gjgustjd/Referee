package com.example.referee.network.model.mediawiki.Single

data class MediaWikiPage(
    val extract: String? = null,
    val pageid: Int? = null,
    val pageimage: String? = null,
    val thumbnail: MediaWikiPageThumbnail? = null,
    val title: String? = null
)