package com.example.weatherappmvi.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteComponentImpl(
    componentContext: ComponentContext,
    private val favoriteStoreFactory: FavoriteStoreFactory,
    private val onCityClicked: (City) -> Unit,
    private val onSearchClicked: () -> Unit,
    private val onAddFavoriteClicked: () -> Unit
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        favoriteStoreFactory.create()
    }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is FavoriteStore.Label.CityItemClick -> {
                        onCityClicked(it.city)
                    }

                    FavoriteStore.Label.SearchClick -> {
                        onSearchClicked()
                    }

                    FavoriteStore.Label.ToFavoriteClick -> {
                        onAddFavoriteClicked()
                    }
                }
            }
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavoriteStore.Intent.SearchClick)
    }

    override fun onClickAddFavorite() {
        store.accept(FavoriteStore.Intent.ToFavoriteClick)
    }

    override fun onCityClick(city: City) {
        store.accept(FavoriteStore.Intent.CityItemClick(city))
    }

    class Factory(
        private val favoriteStoreFactory: FavoriteStoreFactory,
    ) : FavoriteComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onCityClicked: (City) -> Unit,
            onSearchClicked: () -> Unit,
            onAddFavoriteClicked: () -> Unit
        ): FavoriteComponent =
            FavoriteComponentImpl(
                componentContext = componentContext,
                favoriteStoreFactory = favoriteStoreFactory,
                onCityClicked = onCityClicked,
                onSearchClicked = onSearchClicked,
                onAddFavoriteClicked = onAddFavoriteClicked
            )

    }

}