package com.example.weatherappmvi.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.extensions.componentScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsComponentImpl(
    componentContext: ComponentContext,
    private val detailsStoreFactory: DetailsStoreFactory,
    private val city: City,
    private val onBackClicked: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore {
        detailsStoreFactory.create(city)
    }

    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    DetailsStore.Label.BackClick -> onBackClicked()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    override fun onClickBack() {
        store.accept(DetailsStore.Intent.BackClick)
    }

    override fun onClickAddFavorite() {
        store.accept(DetailsStore.Intent.ChangeFavoriteStatusClick)
    }

    class Factory(
        private val detailsStoreFactory: DetailsStoreFactory,
    ) : DetailsComponent.Factory {

        override fun invoke(
            componentContext: ComponentContext,
            city: City,
            onBackClicked: () -> Unit,
        ): DetailsComponent = DetailsComponentImpl(
            componentContext = componentContext,
            detailsStoreFactory = detailsStoreFactory,
            city = city,
            onBackClicked = onBackClicked,
        )
    }
}