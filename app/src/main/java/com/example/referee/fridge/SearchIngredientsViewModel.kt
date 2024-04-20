package com.example.referee.fridge

import androidx.lifecycle.viewModelScope
import com.example.referee.common.CommonUtil
import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.FridgeIngredientEntity
import com.example.referee.fridge.model.FridgeRepository
import com.example.referee.fridge.model.SearchIngredientsEvent
import com.example.referee.network.model.mediawiki.List.Search
import com.example.referee.network.model.mediawiki.MediaWikiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchIngredientsViewModel : BaseViewModel<SearchIngredientsEvent>() {

    fun searchIngredients(keyword: String) {
        MediaWikiRepository.searchAndGetResults(keyword)
            .subscribe({
                val rawStringResult = it.query.searchResults.removeHtmlTags()
                _event.value =
                    EventWrapper(SearchIngredientsEvent.SearchSuccess(rawStringResult))
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

    fun insertIngredientToFridge(entity: FridgeIngredientEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = withContext(Dispatchers.IO) {
                FridgeRepository.insertIngredientToFridge(entity) > 0
            }

            _event.postValue(
                if (result) {
                    EventWrapper(SearchIngredientsEvent.InsertFridgeIngredientSuccess)
                } else {
                    EventWrapper(SearchIngredientsEvent.InsertFridgeIngredientFailure)
                }
            )
        }
    }

    private fun List<Search>.removeHtmlTags(): List<Search> {
        return this.onEach {
            it.snippet = CommonUtil.decodeHtmlEntities(it.snippet.replace(Regex("<[^>]*>"), ""))
        }
    }
}