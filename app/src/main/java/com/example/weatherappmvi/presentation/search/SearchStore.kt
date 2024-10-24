package com.example.weatherappmvi.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.usecase.ChangeFavoriteStateUseCase
import com.example.weatherappmvi.domain.usecase.SearchUseCase
import com.example.weatherappmvi.presentation.search.SearchStore.Intent
import com.example.weatherappmvi.presentation.search.SearchStore.Label
import com.example.weatherappmvi.presentation.search.SearchStore.State
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

interface SearchStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ChangeSearchQuery(val query: String) : Intent
        data object BackCLick : Intent
        data object SearchClick : Intent
        data class CityClick(val city: City) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {
        sealed interface SearchState {
            data object Initial : SearchState
            data object Loading : SearchState
            data object Error : SearchState
            data class SuccessLoaded(val cities: List<City>) : SearchState
            data object EmptyResult : SearchState
        }
    }

    sealed interface Label {
        data object BackClick : Label
        data object SaveToFavorite : Label
        data class OpenForecast(val city: City) : Label
    }
}

class SearchStoreFactory(
    private val storeFactory: StoreFactory,
    private val searchCitiesUseCase: SearchUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
) {

    fun create(
        openReason: OpenReason
    ): SearchStore =
        object : SearchStore, Store<Intent, State, Label> by storeFactory.create(
            name = "SearchStore",
            initialState = State(
                searchQuery = "",
                searchState = State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {
        data class ChangeSearchQuery(val query: String) : Msg

        data object LoadingSearchResult : Msg

        data object SearchResultError : Msg

        data class SearchResultLoaded(val cities: List<City>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
        }
    }

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: Intent) {
            when (intent) {
                Intent.BackCLick -> {
                    publish(Label.BackClick)
                }

                is Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                is Intent.CityClick -> {
                    when (openReason) {
                        OpenReason.AddToFavorite -> {
                            scope.launch {
                                changeFavoriteStateUseCase.addToFavorite(intent.city)
                                publish(Label.SaveToFavorite)
                            }
                        }

                        OpenReason.RegularSearch -> {
                            publish(Label.OpenForecast(intent.city))
                        }
                    }
                }

                Intent.SearchClick -> {
                    dispatch(Msg.LoadingSearchResult)

                    searchJob?.cancel()

                    searchJob = scope.launch {
                        try {
                            val cities = searchCitiesUseCase(state().searchQuery)
                            dispatch(Msg.SearchResultLoaded(cities))
                        } catch (e: Exception) {
                            dispatch(Msg.SearchResultError)
                        }
                    }
                }
            }
        }

    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.ChangeSearchQuery -> {
                copy(searchQuery = msg.query)
            }

            Msg.LoadingSearchResult -> {
                copy(searchState = State.SearchState.Loading)
            }

            Msg.SearchResultError -> {
                copy(searchState = State.SearchState.Error)
            }

            is Msg.SearchResultLoaded -> {
                val searchState = if (msg.cities.isEmpty()) {
                    State.SearchState.EmptyResult
                } else {
                    State.SearchState.SuccessLoaded(msg.cities)
                }
                copy(searchState = searchState)
            }
        }

    }
}
