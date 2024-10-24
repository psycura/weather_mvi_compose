package com.example.weatherappmvi.presentation.details

import com.arkivanov.decompose.ComponentContext
import com.example.weatherappmvi.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onClickBack()

    fun onClickAddFavorite()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            city: City,
            onBackClicked: () -> Unit,
        ): DetailsComponent
    }

}