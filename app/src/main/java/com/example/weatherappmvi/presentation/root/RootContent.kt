package com.example.weatherappmvi.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.example.weatherappmvi.presentation.details.DetailsContent
import com.example.weatherappmvi.presentation.favorite.FavoriteContent
import com.example.weatherappmvi.presentation.search.SearchContent
import com.example.weatherappmvi.presentation.ui.theme.WeatherAppMVITheme

@Composable
fun RootContent(
    component: RootComponent,
) {
    WeatherAppMVITheme {
        Children(stack = component.stack) {
            when (val child = it.instance) {
                is RootComponent.Child.Favorites -> FavoriteContent(component = child.component)
                is RootComponent.Child.Search -> SearchContent(component = child.component)
                is RootComponent.Child.Details -> DetailsContent(component = child.component)
            }
        }
    }
}