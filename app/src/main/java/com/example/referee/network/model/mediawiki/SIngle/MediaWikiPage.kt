package com.example.referee.network.model.mediawiki.SIngle

data class MediaWikiPage(
    val extract: String,
    val ns: Int,
    val pageid: Int,
    val pageimage: String,
    val thumbnail: MediaWikiPageThumbnail,
    val title: String
)