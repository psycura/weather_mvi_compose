package com.example.weatherappmvi.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.example.weatherappmvi.presentation.details.DetailsComponent
import com.example.weatherappmvi.presentation.favorite.FavoriteComponent
import com.example.weatherappmvi.presentation.search.SearchComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    sealed interface Child {
        data class Favorites(val component: FavoriteComponent) : Child
        data class Search(val component: SearchComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }

    interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }

}