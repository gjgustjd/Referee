package com.example.referee.fridge.model

import com.example.referee.network.model.mediawiki.List.Search
import com.example.referee.network.model.mediawiki.SIngle.MediaWikiPage

sealed interface SearchIngredientsEvent {
    class SearchSuccess(val result:List<Search>):SearchIngredientsEvent
    object SearchFailed : SearchIngredientsEvent
    class PageSuccess(val page: MediaWikiPage) : SearchIngredientsEvent
    object PageFailed : SearchIngredientsEvent
}