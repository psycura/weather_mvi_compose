package com.example.weatherappmvi.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun onClickSearch()

    fun onClickAddFavorite()

    fun onCityClick(city: City)

    interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCityClicked: (City) -> Unit,
            onSearchClicked: () -> Unit,
            onAddFavoriteClicked: () -> Unit
        ): FavoriteComponent
    }
}