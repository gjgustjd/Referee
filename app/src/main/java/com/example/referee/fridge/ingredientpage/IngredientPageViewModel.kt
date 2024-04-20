package com.example.referee.fridge.ingredientpage

import com.example.referee.common.EventWrapper
import com.example.referee.common.base.BaseViewModel
import com.example.referee.fridge.model.SearchIngredientsEvent
import com.example.referee.network.model.mediawiki.MediaWikiRepository

class IngredientPageViewModel :BaseViewModel<SearchIngredientsEvent>() {

    fun getIngredientPageData(pageId:Int) {
        MediaWikiRepository.getSinglePageById(pageId)
            .subscribe({
                val pageData = it.query?.pages?.values?.firstOrNull()
                _event.value =
                    pageData?.let {
                        EventWrapper(SearchIngredientsEvent.PageSuccess(it))
                    } ?: run {
                        EventWrapper(SearchIngredientsEvent.PageFailed)
                    }
            }, {
                _event.value =
                    EventWrapper(SearchIngredientsEvent.PageFailed)
            }).addDisposable()
    }
}