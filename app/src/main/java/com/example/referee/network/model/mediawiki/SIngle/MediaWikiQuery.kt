package com.example.referee.network.model.mediawiki.SIngle

data class MediaWikiQuery(
    val pages: Map<String, MediaWikiPage>,
    val redirects: List<MediaWikiQueryRedirect>
)