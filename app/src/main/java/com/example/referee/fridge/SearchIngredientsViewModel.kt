package com.example.referee.fridge

import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.SearchIngredientsEvent
import com.example.referee.network.model.mediawiki.MediaWikiRepository

class SearchIngredientsViewModel : BaseViewModel<SearchIngredientsEvent>() {

    fun searchIngredients(keyword: String) {
        MediaWikiRepository.searchAndGetResults(keyword)
            .subscribe({
                _event.value =
                    EventWrapper(SearchIngredientsEvent.SearchSuccess(it.query.searchResults))
            }, {
                _event.value = EventWrapper(SearchIngredientsEvent.SearchFailed)
            }).addDisposable()
    }

    fun getPageInfo(pageId: String) {
        MediaWikiRepository.getSinglePageById(pageId)
            .subscribe({
                _event.value = it.query.pages.values.firstOrNull()?.let { page ->
                    EventWrapper(SearchIngredientsEvent.PageSuccess(page))
                } ?: EventWrapper(SearchIngredientsEvent.PageFailed)
            }, {
                _event.value = EventWrapper(SearchIngredientsEvent.PageFailed)
            }).addDisposable()
    }
}