package com.example.weatherappmvi.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {
    val model: StateFlow<SearchStore.State>

    fun onSearchQueryChanged(query: String)

    fun onSearchClick()

    fun onCityItemClick(city: City)

    fun onBackClick()

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            openReason: OpenReason,
            onBackClicked: () -> Unit,
            onCityClicked: (City) -> Unit,
            onSaveToFavoriteClicked: () -> Unit
        ): SearchComponent
    }
}