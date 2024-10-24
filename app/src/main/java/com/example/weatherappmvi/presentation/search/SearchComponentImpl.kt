package com.example.weatherappmvi.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchComponentImpl(
    componentContext: ComponentContext,
    private val searchStoreFactory: SearchStoreFactory,
    private val openReason: OpenReason,
    private val onBackClicked: () -> Unit,
    private val onCityClicked: (City) -> Unit,
    private val onSaveToFavoriteClicked: () -> Unit
) : SearchComponent, ComponentContext by componentContext {


    private val store = instanceKeeper.getStore {
        searchStoreFactory.create(openReason)
    }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    SearchStore.Label.BackClick -> {
                        onBackClicked()
                    }
                    is SearchStore.Label.OpenForecast -> {
                        onCityClicked(it.city)
                    }
                    SearchStore.Label.SaveToFavorite -> {
                        onSaveToFavoriteClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun onSearchQueryChanged(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onSearchClick() {
        store.accept(SearchStore.Intent.SearchClick)
    }

    override fun onCityItemClick(city: City) {
        store.accept(SearchStore.Intent.CityClick(city))
    }

    override fun onBackClick() {
        store.accept(SearchStore.Intent.BackCLick)
    }

    class Factory(
        private val searchStoreFactory: SearchStoreFactory,
    ): SearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            openReason: OpenReason,
            onBackClicked: () -> Unit,
            onCityClicked: (City) -> Unit,
            onSaveToFavoriteClicked: () -> Unit
        ): SearchComponent  = SearchComponentImpl(
            componentContext = componentContext,
            searchStoreFactory = searchStoreFactory,
            openReason = openReason,
            onBackClicked = onBackClicked,
            onCityClicked = onCityClicked,
            onSaveToFavoriteClicked = onSaveToFavoriteClicked
        )
    }

}