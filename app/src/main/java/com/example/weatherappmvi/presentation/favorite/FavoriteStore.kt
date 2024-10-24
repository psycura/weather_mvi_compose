package com.example.weatherappmvi.presentation.favorite

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.example.weatherappmvi.domain.entity.City
import com.example.weatherappmvi.domain.usecase.GetFavoriteCitiesUseCase
import com.example.weatherappmvi.domain.usecase.GetWeatherUseCase
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.Intent
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.Label
import com.example.weatherappmvi.presentation.favorite.FavoriteStore.State
import kotlinx.coroutines.launch

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data object SearchClick : Intent
        data object ToFavoriteClick : Intent
        data class CityItemClick(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState,
        )

        sealed interface WeatherState {
            data object Initial : WeatherState
            data object Loading : WeatherState
            data object Error : WeatherState
            data class Loaded(
                val tempC: Float,
                val iconUrl: String,
            ) : WeatherState
        }
    }

    sealed interface Label {
        data object SearchClick : Label
        data object ToFavoriteClick : Label
        data class CityItemClick(val city: City) : Label
    }
}

class FavoriteStoreFactory(
    private val storeFactory: StoreFactory,
    private val getFavoriteCitiesUseCase: GetFavoriteCitiesUseCase,
    private val getWeatherUseCase: GetWeatherUseCase,
) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(listOf()),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class FavoriteCitiesLoaded(val cites: List<City>) : Action
    }

    private sealed interface Msg {

        data class FavoriteCitiesLoaded(val cites: List<City>) : Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val conditionIconUrl: String,
        ) : Msg

        data class WeatherLoadingError(val cityId: Int) : Msg

        data class WeatherIsLoading(val cityId: Int) : Msg


    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteCitiesUseCase().collect {
                    dispatch(Action.FavoriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent) {
            when (intent) {
                is Intent.CityItemClick -> {
                    publish(Label.CityItemClick(intent.city))
                }

                Intent.SearchClick -> {
                    publish(Label.SearchClick)
                }

                Intent.ToFavoriteClick -> {
                    publish(Label.ToFavoriteClick)
                }
            }
        }

        override fun executeAction(action: Action) {
            when (action) {
                is Action.FavoriteCitiesLoaded -> {
                    val cities = action.cites
                    dispatch(Msg.FavoriteCitiesLoaded(cities))

                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getWeatherUseCase(city.id)
                dispatch(Msg.WeatherLoaded(city.id, weather.temperature, weather.iconUrl))
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }

        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteCitiesLoaded -> {
                copy(
                    cityItems = msg.cites.map {
                        State.CityItem(it, State.WeatherState.Initial)
                    }
                )
            }

            is Msg.WeatherIsLoading -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(weatherState = State.WeatherState.Loading)
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoaded -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Loaded(
                                    msg.tempC,
                                    msg.conditionIconUrl
                                )
                            )
                        } else {
                            it
                        }
                    }
                )
            }

            is Msg.WeatherLoadingError -> {
                copy(
                    cityItems = cityItems.map {
                        if (it.city.id == msg.cityId) {
                            it.copy(
                                weatherState = State.WeatherState.Error
                            )
                        } else {
                            it
                        }
                    }
                )
            }
        }

    }
}
