package com.example.weatherappmvi.presentation.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.details.DetailsComponent
import com.example.weatherappmvi.presentation.favorite.FavoriteComponent
import com.example.weatherappmvi.presentation.search.OpenReason
import com.example.weatherappmvi.presentation.search.SearchComponent
import kotlinx.serialization.Serializable

class RootComponentImpl(
    componentContext: ComponentContext,
    private val detailsComponentFactory: DetailsComponent.Factory,
    private val favoriteComponentFactory: FavoriteComponent.Factory,
    private val searchComponentFactory: SearchComponent.Factory
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialConfiguration = Config.Favorites,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child =
        when (config) {
            is Config.Details -> {
                val component = detailsComponentFactory(
                    componentContext = componentContext,
                    city = config.city,
                    onBackClicked = { navigation.pop() }
                )
                RootComponent.Child.Details(component)
            }

            Config.Favorites -> {
                val component = favoriteComponentFactory(
                    componentContext = componentContext,
                    onCityClicked = { navigation.push(Config.Details(it)) },
                    onSearchClicked = { navigation.push(Config.Search(OpenReason.RegularSearch)) },
                    onAddFavoriteClicked = { navigation.push(Config.Search(OpenReason.AddToFavorite)) }
                )
                RootComponent.Child.Favorites(component)
            }

            is Config.Search -> {
                val component = searchComponentFactory(
                    componentContext = componentContext,
                    onCityClicked = { navigation.push(Config.Details(it)) },
                    onBackClicked = { navigation.pop() },
                    openReason = config.openReason,
                    onSaveToFavoriteClicked = { navigation.pop() }
                )
                RootComponent.Child.Search(component)
            }
        }


    @Serializable
    sealed interface Config {

        @Serializable
        data object Favorites : Config

        @Serializable
        data class Search(val openReason: OpenReason) : Config

        @Serializable
        data class Details(val city: City) : Config
    }


    class Factory(
        private val detailsComponentFactory: DetailsComponent.Factory,
        private val favoriteComponentFactory: FavoriteComponent.Factory,
        private val searchComponentFactory: SearchComponent.Factory
    ) : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): RootComponent =
            RootComponentImpl(
                componentContext = componentContext,
                detailsComponentFactory = detailsComponentFactory,
                favoriteComponentFactory = favoriteComponentFactory,
                searchComponentFactory = searchComponentFactory
            )
    }
}