package com.example.referee.network.model.mediawiki.Single

data class MediaWikiQuery(
    val pages: Map<String, MediaWikiPage>,
)