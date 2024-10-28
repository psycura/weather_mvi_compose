package com.example.weatherappmvi.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.weatherappmvi.R
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.presentation.favorite.FavoriteComponent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    component: SearchComponent,
) {

    val state by component.model.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    SearchBar(
        modifier = Modifier.focusRequester(focusRequester),
        query = state.searchQuery,
        onQueryChange = { component.onSearchQueryChanged(it) },
        onSearch = { component.onSearchClick() },
        active = true,
        onActiveChange = { },
        placeholder = {
            Text(stringResource(R.string.search))
        },
        leadingIcon = {
            IconButton(onClick = { component.onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        trailingIcon = {
            IconButton(
                onClick = { component.onSearchClick() }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        },
    ) {
        when (val searchState = state.searchState) {
            SearchStore.State.SearchState.EmptyResult -> {
                Text(
                    stringResource(R.string.empty_result),
                    modifier = Modifier.padding(8.dp)
                )
            }

            SearchStore.State.SearchState.Error -> {
                Text(
                    stringResource(R.string.something_went_wrong),
                    modifier = Modifier.padding(8.dp)
                )
            }

            SearchStore.State.SearchState.Initial -> {}
            SearchStore.State.SearchState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            is SearchStore.State.SearchState.SuccessLoaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        items = searchState.cities,
                        key = { it.id }
                    ) { city ->
                        CityCard(
                            city = city,
                            onCityClick = { component.onCityItemClick(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityCard(
    modifier: Modifier = Modifier,
    city: City,
    onCityClick: (City) -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onCityClick(city) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Text(
                city.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                city.country,
            )
        }
    }
}